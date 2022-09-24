package jjfactory.simpleapi.business.rider.domain;


import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Rider extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    @Column(length = 20)
    private String driverId;

    @Column(length = 20)
    private String name;
    @Column(length = 20, unique = true)
    private String phone;
    private String password;

    private int gender;

    @Column(name = "vcno_hngl_nm",length = 20)
    private String vcNumber; // 차량변호 한글명

    @Column(length = 10)
    private String region; //활동지역 송부

    private String imagePath;
    private String policyNumber;

    private boolean insuranceApply;

    @Builder
    public Rider(Seller seller, String driverId, String name, String phone, String password, int gender, String vcNumber, String region, String imagePath, String policyNumber, boolean insuranceApply) {
        this.seller = seller;
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.vcNumber = vcNumber;
        this.region = region;
        this.imagePath = imagePath;
        this.policyNumber = policyNumber;
        this.insuranceApply = insuranceApply;
    }

    public static Rider create(RiderCreate req, Seller seller){
        return Rider.builder()
                .seller(seller)
                .driverId(req.getDriverId())
                .gender(req.getGender())
                .region(req.getRegion())
                .vcNumber(req.getBikeNumber())
                .phone(req.getPhone())
                .name(req.getName())
                .insuranceApply(false)
                .build();
    }

    public void updateInsuranceApply(boolean insuranceApply) {
        this.insuranceApply = insuranceApply;
    }
}
