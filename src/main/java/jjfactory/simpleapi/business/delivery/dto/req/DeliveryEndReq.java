package jjfactory.simpleapi.business.delivery.dto.req;


import jjfactory.simpleapi.business.delivery.domain.Delivery;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class DeliveryEndReq {
    private String driverId;
    private String deliveryId;
    private String endTime;

    public DeliveryEndReq(String driverId, String deliveryId, String endTime) {
        this.driverId = driverId;
        this.deliveryId = deliveryId;
        this.endTime = endTime;
    }

    public DeliveryEndReq(Delivery delivery) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.driverId = delivery.getRider().getDriverId();
        this.deliveryId = delivery.getDeliveryId();
        this.endTime = delivery.getCompleteTime().format(formatter);
    }
}
