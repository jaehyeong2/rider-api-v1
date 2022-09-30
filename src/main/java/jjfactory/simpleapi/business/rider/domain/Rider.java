package jjfactory.simpleapi.business.rider.domain;


import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Rider extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    @Column(length = 30)
    private String loginId;

    @Comment("암호화된 주민등록번호값")
    private String ssn;

    @Column(length = 20)
    private String driverId;

    @Column(length = 20)
    private String name;
    @Column(length = 20, unique = true)
    private String phone;
    private String password;

    private int gender;

    @Column(name = "vcno_hngl_nm",length = 20)
    private String bikeNumber; // 차량변호 한글명

    @Column(length = 10)
    private String region; //활동지역 송부

    private String imagePath;
    private String policyNumber;

    private boolean insuranceApply;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Rider(Seller seller, String driverId,String loginId, String name, String phone, String password, int gender, String bikeNumber, String region, String imagePath, String policyNumber, boolean insuranceApply,List<Role> roles) {
        this.seller = seller;
        this.driverId = driverId;
        this.loginId = loginId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.bikeNumber = bikeNumber;
        this.region = region;
        this.imagePath = imagePath;
        this.policyNumber = policyNumber;
        this.insuranceApply = insuranceApply;
        this.roles = roles;
    }

    public static Rider create(RiderCreate req, Seller seller,String encPassword){
        return Rider.builder()
                .seller(seller)
                .loginId(req.getLoginId())
                .driverId("DD" + req.getPhone().substring(3))
                .gender(req.getGender())
                .region(req.getRegion())
                .password(encPassword)
                .bikeNumber(req.getBikeNumber())
                .phone(req.getPhone())
                .name(req.getName())
                .insuranceApply(false)
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }

    public void updateInsuranceApply(boolean insuranceApply) {
        this.insuranceApply = insuranceApply;
    }

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
