package jjfactory.simpleapi.business.delivery.service;


import jjfactory.simpleapi.business.balance.domain.BalanceHistory;
import jjfactory.simpleapi.business.balance.repository.BalanceHistoryRepository;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryStartReq;
import jjfactory.simpleapi.business.delivery.dto.res.DeliveryEndRes;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.config.retrofit.RetrofitApi;
import jjfactory.simpleapi.global.config.retrofit.RetrofitConfig;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final DeliveryRepositorySupport deliveryRepositorySupport;

    @Scheduled(cron = "0 0 06 * * *")
    @Transactional(readOnly = true)
    public void findAllDeliveriesToday(){
        List<DeliveryRes> deliveries = deliveryRepositorySupport.findDeliveriesAndBalanceToday();

        try {
            RetrofitConfig<DeliveryStartReq> retrofitConfig = new RetrofitConfig<>();
            retrofitConfig.create(RetrofitApi.class).totalDeliveriesToday(deliveries).execute();

        } catch (IOException e) {
            log.error("error : {}",e);
            throw new BusinessException(ErrorCode.RETROFIT_NETWORK_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public PagingRes<DeliveryRes> findMyDeliveries(Pageable pageable, String startDate, String endDate, Rider rider){
        return new PagingRes(deliveryRepositorySupport.findMyDeliveries(pageable,startDate,endDate,rider.getId()));
    }

    public String deliveryStart(DeliveryCreate dto,Rider rider){
        Delivery delivery = Delivery.create(rider,dto);
        deliveryRepository.save(delivery);

        DeliveryStartReq req = new DeliveryStartReq(delivery);
        RetrofitConfig<DeliveryStartReq> retrofitConfig = new RetrofitConfig<>();

        String body = "";
        try {
             body = retrofitConfig.create(RetrofitApi.class)
                    .drivingStart(req)
                    .execute().body();

        } catch (IOException e) {
            log.error("error : {}",e);
            throw new BusinessException(ErrorCode.RETROFIT_NETWORK_ERROR);
        }

        return body;
    }

    public DeliveryEndRes deliveryEnd(DeliveryEndReq dto,Rider rider) {
        Delivery delivery = getDelivery(dto.getDeliveryId());

        delivery.complete();

        Seller seller = rider.getSeller();

        long seconds = Duration.between(delivery.getPickUpTime(), delivery.getCompleteTime()).getSeconds();

        //1분단위로 금액을 차감하는데, 올림계산 (65초면 2분으로 계산)
        //ceil 메소드 파라미터, 리턴 타입때문에 더블로 형변환
        double minute = Math.ceil((double) seconds/60);

        int chargingPerHour = seller.getChargeRate();

        long balance = (long)(chargingPerHour/60 * minute);

        BalanceHistory balanceHistory = BalanceHistory.create(Long.valueOf(balance).intValue(),delivery);
        balanceHistoryRepository.save(balanceHistory);

        DeliveryEndReq request = new DeliveryEndReq(delivery);
        RetrofitConfig<DeliveryEndReq> retrofitConfig = new RetrofitConfig<>();
        try {
            retrofitConfig.create(RetrofitApi.class).drivingEnd(request).execute();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.RETROFIT_NETWORK_ERROR);
        }

        return new DeliveryEndRes(minute, (int) balance,delivery.getCompleteTime());
    }


    private Delivery getDelivery(String deliveryId) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.DRIVER_ERROR);
        });
        return delivery;
    }

}
