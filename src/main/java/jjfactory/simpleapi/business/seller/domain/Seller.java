package jjfactory.simpleapi.business.seller.domain;


import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seller extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sellerCode;
    private String tell;
    private String name;
    private int chargeRate;
    private String bizNum;

    @Embedded
    private Address address;

    @Builder
    public Seller(String sellerCode, String tell, String name,String bizNum, int chargeRate, Address address) {
        this.sellerCode = sellerCode;
        this.tell = tell;
        this.name = name;
        this.bizNum = bizNum;
        this.chargeRate = chargeRate;
        this.address = address;
    }

    public static Seller create(SellerCreate dto,String sellerCode){
        return Seller.builder()
                .name(dto.getName())
                .tell(dto.getTell())
                .chargeRate(dto.getChargeRate())
                .address(dto.getAddress())
                .bizNum(dto.getBizNum())
                .sellerCode(sellerCode)
                .build();
    }

    public void modify(SellerModify dto) {
        this.tell = dto.getTell();
        this.name = dto.getName();
        this.address = dto.getAddress();
    }

}
