package jjfactory.simpleapi.business.rider.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.dto.res.SellerRiderRes;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jjfactory.simpleapi.business.rider.domain.QRider.*;

@RequiredArgsConstructor
@Repository
public class RiderRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public Rider findRiderByDriverId(String driverId){
        Rider findRider = queryFactory.selectFrom(rider)
                .where(rider.driverId.eq(driverId))
                .fetchOne();

        if(findRider == null) throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        return findRider;
    }

    public Page<SellerRiderRes> findRiderInfoInSeller(Pageable pageable, Long sellerId){
        List<SellerRiderRes> result = queryFactory.select(Projections.constructor(SellerRiderRes.class, rider))
                .from(rider)
                .where(rider.seller.id.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(rider.createDate.desc())
                .fetch();

        int total = queryFactory.select(Projections.constructor(SellerRiderRes.class, rider))
                .from(rider)
                .where(rider.seller.id.eq(sellerId))
                .fetch().size();

        return new PageImpl<>(result,pageable,total);
    }
}
