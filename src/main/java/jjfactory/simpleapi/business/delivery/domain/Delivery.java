package jjfactory.simpleapi.business.delivery.domain;


import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.rider.domain.Rider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "rider_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;
    @Comment("운행으로 인한 차감 보험금")
    private long balance;

    @Embedded
    private Address address;

    @Comment("요청사 명")
    private String clientName;
    @Comment("요청받은 콜 키 값")
    @Column(length = 16)
    private String receiveCallId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Comment(value = "주문 요청시간")
    private LocalDateTime requestTime;

    @Comment(value = "배차시간")
    private LocalDateTime appointTime;

    @Comment(value = "픽업시간")
    private LocalDateTime pickUpTime;

    @Comment(value = "배달 완료시간")
    private LocalDateTime completeTime;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Delivery(Rider rider, long balance, Address address, String receiveCallId, DeliveryStatus deliveryStatus, LocalDateTime requestTime, LocalDateTime appointTime, LocalDateTime pickUpTime, LocalDateTime completeTime, LocalDateTime modifiedDate, String clientName) {
        this.rider = rider;
        this.balance = balance;
        this.address = address;
        this.receiveCallId = receiveCallId;
        this.deliveryStatus = deliveryStatus;
        this.requestTime = requestTime;
        this.appointTime = appointTime;
        this.pickUpTime = pickUpTime;
        this.completeTime = completeTime;
        this.modifiedDate = modifiedDate;
        this.clientName = clientName;
    }

    public static Delivery create(Rider rider, DeliveryCreate dto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Delivery
                .builder()
                .rider(rider)
                .address(Address.builder()
                        .deliveryAddress1(dto.getDeliveryAddress1())
                        .deliveryAddress2(dto.getDeliveryAddress2())
                        .pickUpAddress1(dto.getPickUpAddress1())
                        .pickUpAddress2(dto.getPickUpAddress2())
                        .build())
                .receiveCallId(dto.getCallId())
                .clientName(dto.getClientName())
                .requestTime(LocalDateTime.parse(dto.getRequestTime(),formatter))
                .appointTime(LocalDateTime.parse(dto.getAppointTime(),formatter))
                .deliveryStatus(DeliveryStatus.REQUEST)
                .build();
    }

    public void pickUp() {
        this.pickUpTime = LocalDateTime.now();
    }
    public void complete() {
        this.completeTime = LocalDateTime.now();
    }
    public void updateBalance(long balance) {
        this.balance = balance;
    }

}
