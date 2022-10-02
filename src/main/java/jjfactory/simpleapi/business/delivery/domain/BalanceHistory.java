package jjfactory.simpleapi.business.delivery.domain;


import jjfactory.simpleapi.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BalanceHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;

    @JoinColumn(name = "delivery_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    @Builder
    public BalanceHistory(int balance, Delivery delivery) {
        this.balance = balance;
        this.delivery = delivery;
    }

    public static BalanceHistory create(int balance, Delivery delivery){
        return BalanceHistory.builder()
                .balance(balance)
                .delivery(delivery)
                .build();
    }
}
