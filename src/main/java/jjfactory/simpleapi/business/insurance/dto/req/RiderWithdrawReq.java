package jjfactory.simpleapi.business.insurance.dto.req;


import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RiderWithdrawReq {
    private String driverId;
    private String phone;
    private String name;
    private String ssn;
    private String bikeNumber;

    public RiderWithdrawReq(String driverId, String phone, String name, String ssn, String bikeNumber) {
        this.driverId = driverId;
        this.phone = phone;
        this.name = name;
        this.ssn = ssn;
        this.bikeNumber = bikeNumber;
    }

    public RiderWithdrawReq(Rider rider) {
        this.driverId = rider.getDriverId();
        this.phone = rider.getPhone();
        this.name = rider.getName();
        this.bikeNumber = rider.getBikeNumber();
    }
}
