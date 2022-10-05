package jjfactory.simpleapi.business.seller.dto.req;


import jjfactory.simpleapi.business.delivery.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SellerModify {
    private String name;
    private String tell;
    private Address address;

    @Builder
    public SellerModify(String name, String tell, Address address) {
        this.name = name;
        this.tell = tell;
        this.address = address;
    }
}
