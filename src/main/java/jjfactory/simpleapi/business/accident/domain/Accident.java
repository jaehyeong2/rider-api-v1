package jjfactory.simpleapi.business.accident.domain;


import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Accident extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Comment(value = "사고 접수번호")
    private String claimNumber;

    @Comment(value = "사고 접수시간")
    private LocalDateTime claimTime;
    @Comment(value = "사고 발생시간")
    private LocalDateTime accidentTime;

    @JoinColumn(name = "delivery_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;
    
    @Comment(value = "보상금")
    private int compensation;

    @Builder
    public Accident(String claimNumber, LocalDateTime claimTime, LocalDateTime accidentTime, Delivery delivery, int compensation) {
        this.claimNumber = claimNumber;
        this.claimTime = claimTime;
        this.accidentTime = accidentTime;
        this.delivery = delivery;
        this.compensation = compensation;
    }

    public static Accident create(AccidentCreate dto, Delivery delivery){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Accident.builder()
                .claimTime(LocalDateTime.parse(dto.getClaimTime(),formatter))
                .claimNumber(dto.getClaimNumber())
                .accidentTime(LocalDateTime.parse(dto.getAccidentTime(),formatter))
                .delivery(delivery)
                .build();
    }
}
