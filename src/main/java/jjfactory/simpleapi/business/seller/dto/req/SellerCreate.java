package jjfactory.simpleapi.business.seller.dto.req;


import jjfactory.simpleapi.business.delivery.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SellerCreate {
    private String name;
    private String tell;
    private String bizNum;
    private int chargeRate;
    private Address address;

    @Builder
    public SellerCreate(String name, String tell, String bizNum, int chargeRate, Address address) {
        this.name = name;
        this.tell = tell;
        this.bizNum = bizNum;
        this.chargeRate = chargeRate;
        this.address = address;
    }
}
