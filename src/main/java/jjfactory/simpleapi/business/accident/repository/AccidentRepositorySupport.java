package jjfactory.simpleapi.business.accident.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.domain.QAccident;
import jjfactory.simpleapi.business.accident.dto.res.AccidentRes;
import jjfactory.simpleapi.business.delivery.domain.QDelivery;
import jjfactory.simpleapi.business.rider.domain.QRider;
import jjfactory.simpleapi.business.seller.domain.QSeller;
import jjfactory.simpleapi.business.seller.dto.res.SellerAccidentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

import static jjfactory.simpleapi.business.accident.domain.QAccident.*;
import static jjfactory.simpleapi.business.delivery.domain.QDelivery.*;
import static jjfactory.simpleapi.business.rider.domain.QRider.*;
import static jjfactory.simpleapi.business.seller.domain.QSeller.*;

@RequiredArgsConstructor
@Repository
public class AccidentRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public Page<AccidentRes> findAccidentsByPhone(Pageable pageable, String phone){
        List<AccidentRes> accidents = queryFactory.select(Projections.constructor(AccidentRes.class, accident, delivery))
                .from(accident)
                .innerJoin(delivery).on(accident.delivery.eq(delivery))
                .where(delivery.rider.phone.eq(phone))
                .orderBy(accident.accidentTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory.select(Projections.constructor(AccidentRes.class, accident, delivery))
                .from(accident)
                .innerJoin(delivery).on(accident.delivery.eq(delivery))
                .where(delivery.rider.phone.eq(phone))
                .fetch().size();

        return new PageImpl<>(accidents,pageable,total);
    }

    public Page<SellerAccidentRes> findAccidentsBySellerName(Pageable pageable, String sellerName){
        List<SellerAccidentRes> accidents = queryFactory
                .select(Projections.constructor(SellerAccidentRes.class, accident, delivery, rider))
                .from(accident)
                .innerJoin(delivery).on(accident.delivery.eq(delivery))
                .innerJoin(rider).on(delivery.rider.eq(rider))
                .where(rider.seller.name.eq(sellerName))
                .orderBy(accident.accidentTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory
                .select(Projections.constructor(SellerAccidentRes.class, accident, delivery, rider))
                .from(accident)
                .innerJoin(delivery).on(accident.delivery.eq(delivery))
                .innerJoin(rider).on(delivery.rider.eq(rider))
                .where(rider.seller.name.eq(sellerName))
                .fetch().size();

        return new PageImpl<>(accidents,pageable,total);
    }

    public Integer findTotalCompensation(String sellerName){
        return queryFactory.select(accident.compensation.sum())
                .from(accident)
                .innerJoin(delivery)
                .on(accident.delivery.id.eq(delivery.id))
                .innerJoin(rider)
                .on(delivery.rider.id.eq(rider.id))
                .innerJoin(seller)
                .on(seller.name.eq(sellerName))
                .where(seller.name.eq(sellerName))
                .fetchOne();
    }
}
