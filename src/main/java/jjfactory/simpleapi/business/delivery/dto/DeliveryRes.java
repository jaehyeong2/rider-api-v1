package jjfactory.simpleapi.business.delivery.dto;


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
    private String callRequestTime;

    private String startLocation;
    private String targetLocation;

    @Builder
    public DeliveryRes(String driverId, String deliveryId, String callRequestTime, String startLocation, String targetLocation) {
        this.driverId = driverId;
        this.deliveryId = deliveryId;
        this.callRequestTime = callRequestTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
    }

    public DeliveryRes(Delivery delivery) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.driverId = delivery.getRider().getDriverId();
        this.deliveryId = delivery.getDeliveryId();
        this.callRequestTime = delivery.getRequestTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()+delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()+delivery.getAddress().getDeliveryAddress2();
    }
}
