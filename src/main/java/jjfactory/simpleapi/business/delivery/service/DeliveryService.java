package jjfactory.simpleapi.business.delivery.service;


import jjfactory.simpleapi.business.delivery.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.repository.BalanceHistoryRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Slf4j
@Transactional
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;
    private static String serverUrl;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final DeliveryRepositorySupport deliveryRepositorySupport;

    @Value("${retrofitUrl}")
    public void setServerUrl(String serverUrl) {
        DeliveryService.serverUrl = serverUrl;
    }

    public DeliveryService(DeliveryRepository deliveryRepository, RestTemplateBuilder restTemplateBuilder, BalanceHistoryRepository balanceHistoryRepository, DeliveryRepositorySupport deliveryRepositorySupport) {
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.balanceHistoryRepository = balanceHistoryRepository;
        this.deliveryRepositorySupport = deliveryRepositorySupport;
    }

    @Transactional(readOnly = true)
    public List<DeliveryRes> find3DaysDeliveriesByDriverId(String driverId){
        return deliveryRepositorySupport.findDeliveriesAndBalance3Days(driverId);
    }

    @Scheduled(cron = "0 0 06 * * *")
    @Transactional(readOnly = true)
    public String findAllDeliveriesToday(){
        List<DeliveryRes> deliveries = deliveryRepositorySupport.findDeliveriesAndBalanceToday();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/delivery");
        return restTemplate.postForObject(builder.toUriString(),deliveries,String.class);
    }

    @Transactional(readOnly = true)
    public PagingRes<DeliveryRes> findMyDeliveries(Pageable pageable, String startDate, String endDate, Rider rider){
        return new PagingRes(deliveryRepositorySupport.findMyDeliveries(pageable,startDate,endDate,rider.getId()));
    }

    public String deliveryStart(DeliveryCreate dto,Rider rider) {
        Delivery delivery = Delivery.create(rider,dto);
        deliveryRepository.save(delivery);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/delivery/start");
        DeliveryStartReq request = new DeliveryStartReq(delivery);

        return restTemplate.postForObject(builder.toUriString(),request,String.class);
    }

    public DeliveryEndRes deliveryEnd(DeliveryEndReq dto,Rider rider) {
        Delivery delivery = getDelivery(dto.getDeliveryId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        delivery.complete(LocalDateTime.parse(dto.getEndTime(),formatter));

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

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/delivery/end");

        restTemplate.postForObject(builder.toUriString(),request,String.class);

        return new DeliveryEndRes(minute, (int) balance,delivery.getCompleteTime());
    }

    private Delivery getDelivery(String deliveryId) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.DRIVER_ERROR);
        });
        return delivery;
    }

}
