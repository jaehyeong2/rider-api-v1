package jjfactory.simpleapi.business.balance.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.balance.domain.ReceiveBalance;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static jjfactory.simpleapi.business.balance.domain.QReceiveBalance.receiveBalance;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class ReceiveBalanceRepositorySupportTest {
    @Autowired
    private EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("배치 수신 금액 내역 조회")
    void findReceiveBalances() {
        //given
        for(int i = 1; i<21; i++){
            ReceiveBalance balance = ReceiveBalance.builder()
                    .balance(1000 * i)
                    .receiveDate(LocalDateTime.of(2022,9,1,0,0,0).plusDays((i)))
                    .build();

            em.persist(balance);
        }

        String startDate = "2022-09-10 00:00:00";
        String endDate = "2022-09-30 23:59:59";

        Pageable pageable = new MyPageReq(1,10).of();

        //when
        List<ReceiveBalanceRes> balances = queryFactory.select(Projections.constructor(ReceiveBalanceRes.class, receiveBalance))
                .from(receiveBalance)
                .orderBy(receiveBalance.receiveDate.desc())
                .where(between(startDate,endDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory.select(Projections.constructor(ReceiveBalanceRes.class, receiveBalance))
                .from(receiveBalance)
                .where(between(startDate,endDate))
                .fetch().size();

        //then
        assertThat(total).isEqualTo(12);
        assertThat(balances.get(0).getBatchDate()).isEqualTo("2022-09-21 00:00:00");
    }

    private BooleanExpression between(String startDate, String endDate){
        if (!StringUtils.hasText(startDate) || !StringUtils.hasText(endDate)) {
            return receiveBalance.id.isNotNull();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime convertedStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse(endDate, formatter);

        return receiveBalance.receiveDate.between(convertedStart,convertedEnd);
    }
}