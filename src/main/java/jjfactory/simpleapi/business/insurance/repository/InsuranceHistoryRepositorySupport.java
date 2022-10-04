package jjfactory.simpleapi.business.insurance.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static jjfactory.simpleapi.business.insurance.domain.QInsuranceHistory.*;


@RequiredArgsConstructor
@Repository
public class InsuranceHistoryRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public List<InsuranceHistory> findRequestsByInsuranceStepYesterday(int step){
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(1),LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().minusDays(1),LocalTime.MAX);

        return queryFactory.selectFrom(insuranceHistory)
                .where(insuranceHistory.insuranceStep.eq(step),
                        insuranceHistory.createDate.between(startDate,endDate))
                .fetch();
    }

    public List<InsuranceHistory> findRequestsByInsuranceStepToday(int step){
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(),LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return queryFactory.selectFrom(insuranceHistory)
                .where(insuranceHistory.insuranceStep.eq(step),
                        insuranceHistory.createDate.between(startDate,endDate))
                .fetch();
    }
}
