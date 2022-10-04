package jjfactory.simpleapi.business.insurance.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.insurance.domain.HistoryType;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import jjfactory.simpleapi.business.rider.domain.Rider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static jjfactory.simpleapi.business.insurance.domain.QInsuranceHistory.insuranceHistory;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InsuranceHistoryRepositorySupportTest {
    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    void init(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("어제자 보험가입 요청 조회 성공")
    void findRequestsByInsuranceStepYesterday() {
        //given
        Rider rider = Rider.builder()
                .name("이재형")
                .driverId("dd1234")
                .build();
        em.persist(rider);

        InsuranceHistory history = InsuranceHistory.builder()
                .insuranceStep(2)
                .rider(rider)
                .historyType(HistoryType.REQUEST)
                .createDate(LocalDateTime.now().minusDays(1))
                .build();
        em.persist(history);

        InsuranceHistory history2 = InsuranceHistory.builder()
                .insuranceStep(2)
                .rider(rider)
                .historyType(HistoryType.REQUEST)
                .createDate(LocalDateTime.now().minusDays(2))
                .build();
        em.persist(history2);

        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().minusDays(1),LocalTime.MAX);

        //when
        List<InsuranceHistory> result = queryFactory.selectFrom(insuranceHistory)
                .where(insuranceHistory.insuranceStep.eq(2),
                        insuranceHistory.createDate.between(startDate, endDate))
                .fetch();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getRider()).isEqualTo(rider);
    }
}