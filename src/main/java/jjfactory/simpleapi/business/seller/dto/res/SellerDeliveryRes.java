package jjfactory.simpleapi.business.seller.dto.res;


import jjfactory.simpleapi.business.delivery.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class SellerDeliveryRes {
    private String deliveryId;
    private String appointTime;
    private String startLocation;
    private String targetLocation;
    private int balance;
    private String riderLoginId;

    @Builder
    public SellerDeliveryRes(String deliveryId, String appointTime, String startLocation, String targetLocation) {
        this.deliveryId = deliveryId;
        this.appointTime = appointTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
    }

    public SellerDeliveryRes(Delivery delivery, BalanceHistory balanceHistory, Rider rider) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.balance = balanceHistory.getBalance();
        this.riderLoginId = rider.getLoginId();
        this.deliveryId = delivery.getDeliveryId();
        this.appointTime = delivery.getAppointTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()+delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()+delivery.getAddress().getDeliveryAddress2();
    }
}
