package jjfactory.simpleapi.business.balance.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static jjfactory.simpleapi.business.balance.domain.QReceiveBalance.*;

@Repository
@RequiredArgsConstructor
public class ReceiveBalanceRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public Page<ReceiveBalanceRes> findReceiveBalances(Pageable pageable, String startDate, String endDate){
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

        return new PageImpl<>(balances,pageable,total);
    }

    private BooleanExpression between(String startDate,String endDate){
        if (!StringUtils.hasText(startDate) || !StringUtils.hasText(endDate)) {
            return receiveBalance.id.isNotNull();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime convertedStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse(endDate, formatter);

        return receiveBalance.receiveDate.between(convertedStart,convertedEnd);
    }
}
