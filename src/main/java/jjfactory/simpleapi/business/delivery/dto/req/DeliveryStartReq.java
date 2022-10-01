package jjfactory.simpleapi.business.delivery.dto.req;


import jjfactory.simpleapi.business.delivery.domain.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor
@Getter
public class DeliveryStartReq {
    private String driverId;
    private String deliveryId;
    private String startTime;

    @Builder
    public DeliveryStartReq(String driverId, String deliveryId, String startTime) {
        this.driverId = driverId;
        this.deliveryId = deliveryId;
        this.startTime = startTime;
    }

    public DeliveryStartReq(Delivery delivery) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.driverId = delivery.getRider().getDriverId();
        this.deliveryId = delivery.getDeliveryId();
        this.startTime = delivery.getAppointTime().format(formatter);
    }
}
