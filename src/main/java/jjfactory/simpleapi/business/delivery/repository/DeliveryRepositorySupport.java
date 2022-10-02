package jjfactory.simpleapi.business.delivery.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import jjfactory.simpleapi.business.delivery.dto.RiderDeliveryRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static jjfactory.simpleapi.business.delivery.domain.QBalanceHistory.balanceHistory;
import static jjfactory.simpleapi.business.delivery.domain.QDelivery.*;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositorySupport {
    private final JPAQueryFactory queryFactory;


    //TODO 사고시간이랑 엮인 운행을 찾아야 함. 배차시간이 사고시간보다 전. 종료시간은 없을 수 도 있음(사고나서 종료 못 친 경우).
    public List<RiderDeliveryRes> findDeliveriesByAccidentTime(Accident accident){
        return queryFactory.select(Projections.constructor(RiderDeliveryRes.class,delivery))
                .from(delivery)
                .where(delivery.appointTime.before(accident.getAccidentTime()))
                .fetch();
    }

    public List<DeliveryRes> findDeliveriesAndBalanceToday(){
        return  queryFactory.select(Projections.constructor(DeliveryRes.class, delivery, balanceHistory))
                .from(delivery)
                .join(balanceHistory)
                .on(delivery.eq(balanceHistory.delivery))
                .where(betweenToday())
                .orderBy(delivery.appointTime.desc())
                .fetch();
    }

    public List<DeliveryRes> findDeliveriesAndBalance3Days(String driverId){
        return  queryFactory.select(Projections.constructor(DeliveryRes.class, delivery, balanceHistory))
                .from(delivery)
                .join(balanceHistory)
                .on(delivery.eq(balanceHistory.delivery))
                .where(between3Days(),
                        delivery.rider.driverId.eq(driverId))
                .orderBy(delivery.appointTime.desc())
                .fetch();
    }

    public Page<RiderDeliveryRes> findMyDeliveries(Pageable pageable,String startDate, String endDate, Long riderId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime convertedStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse(endDate, formatter);

        List<RiderDeliveryRes> deliveries = queryFactory.select(Projections.constructor(RiderDeliveryRes.class, delivery))
                .from(delivery)
                .where(delivery.appointTime.between(convertedStart, convertedEnd),
                        delivery.id.isNotNull(),
                        delivery.rider.id.eq(riderId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(delivery.appointTime.desc())
                .fetch();

        int count = queryFactory.select(Projections.constructor(RiderDeliveryRes.class, delivery))
                .from(delivery)
                .where(delivery.appointTime.between(convertedStart, convertedEnd),
                        delivery.id.isNotNull(),
                        delivery.rider.id.eq(riderId))
                .fetch().size();

        return new PageImpl<>(deliveries,pageable,count);
    }

    private BooleanExpression betweenToday() {
        return delivery.appointTime.between(
                LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(6,0,0))
                , LocalDateTime.of(LocalDate.now(), LocalTime.of(5,59,59)));
    }

    private BooleanExpression between3Days() {
        return delivery.appointTime.between(LocalDateTime.now().minusDays(3),LocalDateTime.now());
    }
}
