package jjfactory.simpleapi.business.delivery.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String pickUpAddress1;
    private String pickUpAddress2;

    private String deliveryAddress1;
    private String deliveryAddress2;

    @Builder
    public Address(String pickUpAddress1, String pickUpAddress2, String deliveryAddress1, String deliveryAddress2) {
        this.pickUpAddress1 = pickUpAddress1;
        this.pickUpAddress2 = pickUpAddress2;
        this.deliveryAddress1 = deliveryAddress1;
        this.deliveryAddress2 = deliveryAddress2;
    }
}
