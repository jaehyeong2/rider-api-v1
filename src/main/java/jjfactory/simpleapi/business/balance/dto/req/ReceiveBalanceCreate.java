package jjfactory.simpleapi.business.balance.dto.req;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReceiveBalanceCreate {
    private int balance;
    private String batchDate;

    public ReceiveBalanceCreate(int balance, String batchDate) {
        this.balance = balance;
        this.batchDate = batchDate;
    }
}
