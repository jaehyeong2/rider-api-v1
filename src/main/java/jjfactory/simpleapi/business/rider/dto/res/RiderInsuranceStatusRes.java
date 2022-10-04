package jjfactory.simpleapi.business.rider.dto.res;


import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RiderInsuranceStatusRes {
    private String driverId;
    private boolean insureApply;


    public RiderInsuranceStatusRes(String driverId, boolean insureApply) {
        this.driverId = driverId;
        this.insureApply = insureApply;
    }

    public RiderInsuranceStatusRes(Rider rider) {
        this.driverId = rider.getDriverId();
        this.insureApply = rider.isInsuranceApply();
    }
}
