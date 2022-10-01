package jjfactory.simpleapi.business.delivery.dto;


import jjfactory.simpleapi.business.delivery.domain.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class RiderDeliveryRes {
    private String deliveryId;
    private String appointTime;

    private String startLocation;
    private String targetLocation;

    @Builder
    public RiderDeliveryRes(String deliveryId, String appointTime, String startLocation, String targetLocation) {
        this.deliveryId = deliveryId;
        this.appointTime = appointTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
    }

    public RiderDeliveryRes(Delivery delivery) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.deliveryId = delivery.getDeliveryId();
        this.appointTime = delivery.getAppointTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()+delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()+delivery.getAddress().getDeliveryAddress2();
    }
}
