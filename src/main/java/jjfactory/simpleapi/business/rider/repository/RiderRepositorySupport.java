package jjfactory.simpleapi.business.rider.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
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


    public List<Rider> findRidersInDriverIds(List<String> driverIds){
        return queryFactory.selectFrom(rider)
                .where(rider.driverId.in(driverIds))
                .fetch();
    }
}
