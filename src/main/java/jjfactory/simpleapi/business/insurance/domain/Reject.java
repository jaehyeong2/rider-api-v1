package jjfactory.simpleapi.business.insurance.domain;


import jjfactory.simpleapi.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reject extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "history_id")
    @OneToOne(fetch = FetchType.LAZY)
    private InsuranceHistory insuranceHistory;

    private String reason;

    @Builder
    public Reject(InsuranceHistory insuranceHistory, String reason) {
        this.insuranceHistory = insuranceHistory;
        this.reason = reason;
    }

    public static Reject create(InsuranceHistory history,String reason){
        return Reject.builder()
                .insuranceHistory(history)
                .reason(reason)
                .build();
    }
}
