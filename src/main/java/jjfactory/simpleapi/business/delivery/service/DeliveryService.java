package jjfactory.simpleapi.business.delivery.service;


import jjfactory.simpleapi.business.balance.domain.BalanceHistory;
import jjfactory.simpleapi.business.balance.repository.BalanceHistoryRepository;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryStartReq;
import jjfactory.simpleapi.business.delivery.dto.res.DeliveryEndRes;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.config.retrofit.RetrofitApi;
import jjfactory.simpleapi.global.config.retrofit.RetrofitConfig;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Transactional
@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final RiderRepository riderRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    public String deliveryStart(DeliveryCreate dto) throws Exception {
        Rider rider = getRider(dto.getDriverId());

        Delivery delivery = Delivery.create(rider,dto);
        deliveryRepository.save(delivery);

        DeliveryStartReq req = new DeliveryStartReq(delivery);
        RetrofitConfig<DeliveryStartReq> retrofitConfig = new RetrofitConfig<>();

        retrofitConfig.create(RetrofitApi.class).drivingStart(req).execute();

        return "ok";
    }

    //운행종료
    public DeliveryEndRes deliveryEnd(DeliveryEndReq dto) throws Exception {
        Delivery delivery = getDelivery(dto.getDeliveryId());

        delivery.complete();

        Rider rider = delivery.getRider();
        Seller seller = rider.getSeller();

        long seconds = Duration.between(delivery.getPickUpTime(), delivery.getCompleteTime()).getSeconds();

        //1분단위로 금액을 차감하는데, 올림계산 (65초면 2분으로 계산)
        //ceil 메소드 파라미터, 리턴 타입때문에 더블로 형변환
        double minute = Math.ceil((double) seconds/60);

        int chargingPerHour = seller.getChargeRate();

        long balance = (long)(chargingPerHour/60 * minute);

        delivery.updateBalance(balance);

        BalanceHistory balanceHistory = BalanceHistory.create(Long.valueOf(balance).intValue(),delivery);
        balanceHistoryRepository.save(balanceHistory);

        DeliveryEndReq request = new DeliveryEndReq(delivery);
        RetrofitConfig<DeliveryEndReq> retrofitConfig = new RetrofitConfig<>();
        retrofitConfig.create(RetrofitApi.class).drivingEnd(request).execute();

        return new DeliveryEndRes(minute, (int) balance,delivery.getCompleteTime());
    }

    private LocalDateTime convertDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date,formatter);
    }

    private Delivery getDelivery(String deliveryId) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.DRIVER_ERROR);
        });
        return delivery;
    }

    private Rider getRider(String driverId) {
        return riderRepository.findByDriverId(driverId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });
    }
}
