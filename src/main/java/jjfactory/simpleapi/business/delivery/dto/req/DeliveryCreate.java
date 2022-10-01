package jjfactory.simpleapi.business.delivery.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeliveryCreate {
    private String deliveryAddress1;
    private String deliveryAddress2;
    private String pickUpAddress1;
    private String pickUpAddress2;
    private String clientName;
    private String receiveId;
    private String driverId;

    private String requestTime;
    private String appointTime;

    @Builder
    public DeliveryCreate(String deliveryAddress1, String deliveryAddress2, String pickUpAddress1, String pickUpAddress2, String clientName, String receiveId, String requestTime, String appointTime, String driverId) {
        this.deliveryAddress1 = deliveryAddress1;
        this.deliveryAddress2 = deliveryAddress2;
        this.pickUpAddress1 = pickUpAddress1;
        this.pickUpAddress2 = pickUpAddress2;
        this.clientName = clientName;
        this.receiveId = receiveId;
        this.driverId = driverId;
        this.requestTime = requestTime;
        this.appointTime = appointTime;
    }
}
