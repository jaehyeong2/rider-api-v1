package jjfactory.simpleapi.business.accident.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.domain.QAccident;
import jjfactory.simpleapi.business.delivery.domain.QDelivery;
import jjfactory.simpleapi.business.rider.domain.QRider;
import jjfactory.simpleapi.business.seller.domain.QSeller;
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

    public Page<Accident> findAccidentsByPhone(Pageable pageable, String phone){
        List<Accident> accidents = queryFactory.selectFrom(accident)
                .where(accident.delivery.rider.phone.eq(phone))
                .orderBy(accident.accidentTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory.selectFrom(accident)
                .where(accident.delivery.rider.phone.eq(phone))
                .orderBy(accident.accidentTime.desc())
                .fetch().size();

        return new PageImpl<>(accidents,pageable,total);
    }

    public Integer findTotalCompensation(String sellerCode){
        return queryFactory.select(accident.compensation.sum())
                .from(accident)
                .innerJoin(delivery)
                .on(accident.delivery.id.eq(delivery.id))
                .innerJoin(rider)
                .on(delivery.rider.id.eq(rider.id))
                .innerJoin(seller)
                .on(seller.sellerCode.eq(sellerCode))
                .where(seller.sellerCode.eq(sellerCode))
                .fetchOne();
    }
}
