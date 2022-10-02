package jjfactory.simpleapi.business.balance.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ReceiveBalanceCreate {
    @NotNull
    private int balance;
    @NotEmpty
    private String batchDate;

    @Builder
    public ReceiveBalanceCreate(int balance, String batchDate) {
        this.balance = balance;
        this.batchDate = batchDate;
    }
}
