package jjfactory.simpleapi.business.balance.dto.res;


import jjfactory.simpleapi.business.balance.domain.ReceiveBalance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class ReceiveBalanceRes {
    private int balance;
    private String batchDate;

    @Builder
    public ReceiveBalanceRes(int balance, String batchDate) {
        this.balance = balance;
        this.batchDate = batchDate;
    }

    public ReceiveBalanceRes(ReceiveBalance balance) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.balance = balance.getBalance();
        this.batchDate = balance.getReceiveDate().format(formatter);
    }
}
