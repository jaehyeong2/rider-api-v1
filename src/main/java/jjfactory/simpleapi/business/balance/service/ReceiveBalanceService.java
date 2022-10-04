package jjfactory.simpleapi.business.balance.service;


import jjfactory.simpleapi.business.balance.domain.ReceiveBalance;
import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import jjfactory.simpleapi.business.balance.repository.ReceiveBalanceRepository;
import jjfactory.simpleapi.business.balance.repository.ReceiveBalanceRepositorySupport;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class ReceiveBalanceService {
    private final ReceiveBalanceRepository receiveBalanceRepository;
    private final ReceiveBalanceRepositorySupport receiveBalanceRepositorySupport;

    public String receiveBatchBalance(ReceiveBalanceCreate dto){
        ReceiveBalance receiveBalance = ReceiveBalance.create(dto);
        receiveBalanceRepository.save(receiveBalance);
        return "ok";
    }

    @Transactional(readOnly = true)
    public PagingRes<ReceiveBalanceRes> findBatchBalances(Pageable pageable, String startDate, String endDate){
        return new PagingRes<>(receiveBalanceRepositorySupport.findReceiveBalances(pageable,startDate,endDate));
    }

}
