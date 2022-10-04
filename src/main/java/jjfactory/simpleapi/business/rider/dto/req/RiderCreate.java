package jjfactory.simpleapi.business.rider.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class RiderCreate {
    @NotBlank
    private String loginId;
    @NotBlank
    private String phone;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String ssn;
    @NotNull
    private int gender;
    @NotBlank
    private String bikeNumber;
    @NotBlank
    private String region;
    @NotBlank
    private String sellerCode;

    @Builder
    public RiderCreate(String loginId, String phone, String name, String ssn, int gender, String bikeNumber, String region,String sellerCode,String password) {
        this.loginId = loginId;
        this.phone = phone;
        this.name = name;
        this.ssn = ssn;
        this.gender = gender;
        this.bikeNumber = bikeNumber;
        this.password = password;
        this.region = region;
        this.sellerCode = sellerCode;
    }
}
