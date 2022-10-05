package jjfactory.simpleapi.business.seller.dto.res;


import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class SellerRiderRes {
    private String riderName;
    private boolean insuranceStatus;
    private String loginId;
    private String driverId;
    private String signUpDate;

    @Builder
    public SellerRiderRes(String riderName, boolean insuranceStatus, String loginId, String driverId, String signUpDate) {
        this.riderName = riderName;
        this.insuranceStatus = insuranceStatus;
        this.loginId = loginId;
        this.driverId = driverId;
        this.signUpDate = signUpDate;
    }

    public SellerRiderRes(Rider rider) {
        this.riderName = rider.getName();
        this.insuranceStatus = rider.isInsuranceApply();
        this.loginId = rider.getLoginId();
        this.driverId = rider.getDriverId();
        this.signUpDate = rider.getCreateDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
