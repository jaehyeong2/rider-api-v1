package jjfactory.simpleapi.business.balance.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.balance.domain.QBalanceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jjfactory.simpleapi.business.balance.domain.QBalanceHistory.*;

@Repository
@RequiredArgsConstructor
public class BalanceRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public Integer findMyBalances(String startDate,String endDate,String driverId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime convertedStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse(endDate, formatter);

        return queryFactory.select(balanceHistory.balance.sum())
                .from(balanceHistory)
                .where(balanceHistory.delivery.appointTime.between(convertedStart,convertedEnd),
                        balanceHistory.delivery.rider.driverId.eq(driverId))
                .fetchOne();
    }
}
