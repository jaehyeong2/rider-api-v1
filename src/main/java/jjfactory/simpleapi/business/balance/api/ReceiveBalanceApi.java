package jjfactory.simpleapi.business.balance.api;


import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import jjfactory.simpleapi.business.balance.service.ReceiveBalanceService;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.ApiPageRes;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/balance")
@RequiredArgsConstructor
@RestController
public class ReceiveBalanceApi {
    private final ReceiveBalanceService receiveBalanceService;

    @PostMapping
    public ApiRes<String> receiveBatchBalance(@RequestBody ReceiveBalanceCreate dto){
        return new ApiRes<>(receiveBalanceService.receiveBatchBalance(dto));
    }

    @GetMapping
    public ApiPageRes<ReceiveBalanceRes> findReceiveBalances(@RequestParam(required = false, defaultValue = "1") int page,
                                                             @RequestParam(required = false,defaultValue = "10")int size,
                                                             @RequestParam(required = false) String startDate,
                                                             @RequestParam(required = false) String endDate){
        return new ApiPageRes<>(receiveBalanceService.findBatchBalances(new MyPageReq(page,size).of(),startDate,endDate));
    }

}
