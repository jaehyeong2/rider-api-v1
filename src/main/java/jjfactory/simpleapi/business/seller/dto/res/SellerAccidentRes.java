package jjfactory.simpleapi.business.seller.dto.res;

import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class SellerAccidentRes {
    private String deliveryId;
    private String riderName;
    private String accidentTime;
    private String startLocation;
    private String targetLocation;
    private int compensation;

    @Builder
    public SellerAccidentRes(String deliveryId, String accidentTime, String startLocation, String targetLocation, int compensation) {
        this.deliveryId = deliveryId;
        this.accidentTime = accidentTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
        this.compensation = compensation;
    }

    public SellerAccidentRes(Accident accident, Delivery delivery, Rider rider) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.riderName = rider.getName();
        this.deliveryId = delivery.getDeliveryId();
        this.accidentTime = accident.getAccidentTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()
                + delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()
                + delivery.getAddress().getDeliveryAddress2();
        this.compensation = accident.getCompensation();
    }

}
