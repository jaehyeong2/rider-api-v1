package jjfactory.simpleapi.domain.delivery.dto.req;


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
    private String callId;

    private String requestTime;
    private String appointTime;


    public DeliveryCreate(String deliveryAddress1, String deliveryAddress2, String pickUpAddress1, String pickUpAddress2, String clientName, String callId, String requestTime, String appointTime) {
        this.deliveryAddress1 = deliveryAddress1;
        this.deliveryAddress2 = deliveryAddress2;
        this.pickUpAddress1 = pickUpAddress1;
        this.pickUpAddress2 = pickUpAddress2;
        this.clientName = clientName;
        this.callId = callId;
        this.requestTime = requestTime;
        this.appointTime = appointTime;
    }
}
