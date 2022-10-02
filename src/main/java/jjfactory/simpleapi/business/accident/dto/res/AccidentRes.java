package jjfactory.simpleapi.business.accident.dto.res;

import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class AccidentRes {
    private String deliveryId;
    private String accidentTime;
    private String startLocation;
    private String targetLocation;
    private int compensation;

    @Builder
    public AccidentRes(String deliveryId, String accidentTime, String startLocation, String targetLocation, int compensation) {
        this.deliveryId = deliveryId;
        this.accidentTime = accidentTime;
        this.startLocation = startLocation;
        this.targetLocation = targetLocation;
        this.compensation = compensation;
    }

    public AccidentRes(Accident accident, Delivery delivery) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.deliveryId = delivery.getDeliveryId();
        this.accidentTime = accident.getAccidentTime().format(formatter);
        this.startLocation = delivery.getAddress().getPickUpAddress1()
                + delivery.getAddress().getPickUpAddress2();
        this.targetLocation = delivery.getAddress().getDeliveryAddress1()
                + delivery.getAddress().getDeliveryAddress2();
        this.compensation = accident.getCompensation();
    }

}
