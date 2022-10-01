package jjfactory.simpleapi.business.delivery.dto;


import jjfactory.simpleapi.business.balance.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class DeliveryRes {
    private String driverId;
    private String deliveryId;
    private String appointTime;

    private String startLocation;
    private String targetLocation;
    private int balance;

    @Builder
    public DeliveryRes(String driverId, String deliveryId, String appointTime, String startLocation, String targetLocation, int balance) {
        this.driverId = driverId;
        this.deliveryId = deliveryId;
        this.appointTime = appointTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
        this.balance = balance;
    }

    public DeliveryRes(Delivery delivery, BalanceHistory balanceHistory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.driverId = delivery.getRider().getDriverId();
        this.balance = balanceHistory.getBalance();
        this.deliveryId = delivery.getDeliveryId();
        this.appointTime = delivery.getAppointTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()+delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()+delivery.getAddress().getDeliveryAddress2();
    }
}
