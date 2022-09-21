package jjfactory.simpleapi.business.rider.dto.req;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RiderCreate {
    private String driverId;
    private String phone;
    private String name;
    private String ssn;
    private int gender;
    private String bikeNumber;
    private String region;
}
