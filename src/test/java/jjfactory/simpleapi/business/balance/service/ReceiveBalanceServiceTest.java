package jjfactory.simpleapi.business.balance.service;

import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import jjfactory.simpleapi.business.balance.repository.ReceiveBalanceRepository;
import jjfactory.simpleapi.business.balance.repository.ReceiveBalanceRepositorySupport;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveBalanceServiceTest {
    @InjectMocks
    ReceiveBalanceService receiveBalanceService;
    @Mock
    ReceiveBalanceRepositorySupport receiveBalanceRepositorySupport;
    @Spy
    ReceiveBalanceRepository receiveBalanceRepository;

    @Test
    @DisplayName("금액내역 배치수신 성공")
    void receiveBatchBalance() {
        //given
        ReceiveBalanceCreate dto = ReceiveBalanceCreate.builder()
                .balance(1000000).batchDate("2022-10-04 10:00:00")
                .build();

        //when
        String result = receiveBalanceService.receiveBatchBalance(dto);

        //then
        assertThat(result).isEqualTo("ok");
    }

    @Test
    @DisplayName("배치 수신 금액내역 조회 성공")
    void findBatchBalances() {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();
        String startDate = "2022-01-01 00:00:00";
        String endDate = "2022-12-31 23:59:59";

        ReceiveBalanceRes res = ReceiveBalanceRes.builder()
                .balance(1000).batchDate("2022-07-07 11:11:11")
                .build();

        List<ReceiveBalanceRes> list = List.of(res);

        //stub
        when(receiveBalanceRepositorySupport.findReceiveBalances(pageable,startDate,endDate))
                .thenReturn(new PageImpl<>(list,pageable,list.size()));

        //when
        PagingRes<ReceiveBalanceRes> result = receiveBalanceService.findBatchBalances(pageable, startDate, endDate);

        //then
        assertThat(result.getResultList().get(0).getBalance()).isEqualTo(1000);
        assertThat(result.getResultList().get(0).getBatchDate()).isEqualTo("2022-07-07 11:11:11");
    }
}