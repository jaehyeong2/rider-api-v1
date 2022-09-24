package jjfactory.simpleapi.business.balance.domain;


import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReceiveBalance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;

    @CreatedDate
    private LocalDateTime receiveDate;

    @Builder
    public ReceiveBalance(int balance, LocalDateTime receiveDate) {
        this.balance = balance;
        this.receiveDate = receiveDate;
    }

    public static ReceiveBalance create(ReceiveBalanceCreate dto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ReceiveBalance.builder()
                .balance(dto.getBalance())
                .receiveDate(LocalDateTime.parse(dto.getBatchDate(),formatter))
                .build();
    }
}
