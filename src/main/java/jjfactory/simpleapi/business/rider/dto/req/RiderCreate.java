package jjfactory.simpleapi.business.rider.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RiderCreate {
    private String loginId;
    private String phone;
    private String name;
    private String ssn;
    private int gender;
    private String bikeNumber;
    private String region;
    private String sellerCode;

    @Builder
    public RiderCreate(String loginId, String phone, String name, String ssn, int gender, String bikeNumber, String region,String sellerCode) {
        this.loginId = loginId;
        this.phone = phone;
        this.name = name;
        this.ssn = ssn;
        this.gender = gender;
        this.bikeNumber = bikeNumber;
        this.region = region;
        this.sellerCode = sellerCode;
    }
}
