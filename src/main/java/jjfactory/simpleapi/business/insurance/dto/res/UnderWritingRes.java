package jjfactory.simpleapi.business.insurance.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnderWritingRes {
    private String driverId;
    private String bikeNumber;
    private String result;

    @Builder
    public UnderWritingRes(String driverId, String bikeNumber, String result) {
        this.driverId = driverId;
        this.bikeNumber = bikeNumber;
        this.result = result;
    }
}
