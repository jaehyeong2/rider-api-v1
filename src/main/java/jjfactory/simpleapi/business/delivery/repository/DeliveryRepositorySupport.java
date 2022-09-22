package jjfactory.simpleapi.business.delivery.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.delivery.domain.QDelivery;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jjfactory.simpleapi.business.delivery.domain.QDelivery.*;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositorySupport {
    private final JPAQueryFactory queryFactory;


    //TODO 사고시간이랑 엮인 운행을 찾아야 함. 배차시간이 사고시간보다 전. 종료시간은 없을 수 도 있음(사고나서 종료 못 친 경우).
    public List<DeliveryRes> findDeliveriesByAccidentTime(Accident accident){
        return queryFactory.select(Projections.constructor(DeliveryRes.class,delivery))
                .from(delivery)
                .where(delivery.appointTime.before(accident.getAccidentTime()))
                .fetch();
    }
}
