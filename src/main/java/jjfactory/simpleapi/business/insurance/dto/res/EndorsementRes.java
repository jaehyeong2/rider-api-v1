package jjfactory.simpleapi.business.insurance.dto.res;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EndorsementRes {
    private String driverId;
    private String bikeNumber;
    private String result;
    private String effectiveTime;

    @Builder
    public EndorsementRes(String driverId, String bikeNumber, String result, String effectiveTime) {
        this.driverId = driverId;
        this.bikeNumber = bikeNumber;
        this.result = result;
        this.effectiveTime = effectiveTime;
    }
}
