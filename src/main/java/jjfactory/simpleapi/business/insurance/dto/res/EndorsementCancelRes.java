package jjfactory.simpleapi.business.insurance.dto.res;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EndorsementCancelRes {
    private String driverId;
    private String bikeNumber;
    private String result;

    @Builder
    public EndorsementCancelRes(String driverId, String bikeNumber, String result) {
        this.driverId = driverId;
        this.bikeNumber = bikeNumber;
        this.result = result;
    }
}
