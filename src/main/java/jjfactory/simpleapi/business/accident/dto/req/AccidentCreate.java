package jjfactory.simpleapi.business.accident.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class AccidentCreate {
    @NotEmpty
    private String deliveryId;
    @NotEmpty
    private String claimNumber;
    @NotEmpty
    private String claimTime;
    @NotEmpty
    private String accidentTime;

    @Builder
    public AccidentCreate(String deliveryId, String claimNumber, String claimTime, String accident_time) {
        this.deliveryId = deliveryId;
        this.claimNumber = claimNumber;
        this.claimTime = claimTime;
        this.accidentTime = accident_time;
    }
}
