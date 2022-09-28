package jjfactory.simpleapi.business.delivery.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@Getter
public class DeliveryEndRes {
    private String runTime;
    private String endDate;
    private int balance;

    @Builder
    public DeliveryEndRes(String runTime, String endDate, int balance) {
        this.runTime = runTime;
        this.endDate = endDate;
        this.balance = balance;
    }

    public DeliveryEndRes(Double runTime, int balance, LocalDateTime endTime) {
        this.runTime = runTime.toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.endDate = endTime.format(formatter);
        this.balance = balance;
    }
}
