package jjfactory.simpleapi.business.insurance.dto.req;


import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
public class UnderWritingReq {
    private String driverId;
    private String name;
    private String ssn;
    private String bikeNumber;
    private String region;

    public UnderWritingReq(String driverId, String name, String ssn, String bikeNumber, String region) {
        this.driverId = driverId;
        this.name = name;
        this.ssn = ssn;
        this.bikeNumber = bikeNumber;
        this.region = region;
    }

    public UnderWritingReq(Rider rider){
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
