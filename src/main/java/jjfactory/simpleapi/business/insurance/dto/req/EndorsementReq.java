package jjfactory.simpleapi.business.insurance.dto.req;


import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EndorsementReq {
    private String driverId;
    private String name;
    private String ssn;
    private String bikeNumber;
    private String region;

    public EndorsementReq(String driverId, String name, String ssn, String bikeNumber, String region) {
        this.driverId = driverId;
        this.name = name;
        this.ssn = ssn;
        this.bikeNumber = bikeNumber;
        this.region = region;
    }

    public EndorsementReq(Rider rider){
        this.driverId = rider.getDriverId();
        this.name = rider.getName();
        this.ssn = rider.getSsn();
        this.bikeNumber = rider.getBikeNumber();
        this.region = rider.getRegion();
    }

    public void updateSsn(String ssn) {
        this.ssn = ssn;
    }
}
