package jjfactory.simpleapi.business.balance.api;


import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "예납금 차감내역 저장", notes = "서버로부터 받은 예납금 차감내역 값을 저장합니다.")
    public ApiRes<String> receiveBatchBalance(@RequestBody ReceiveBalanceCreate dto){
        return new ApiRes<>(receiveBalanceService.receiveBatchBalance(dto));
    }

    @GetMapping
    @ApiOperation(value = "예납금 차감내역 조회", notes = "수신한 예납금 차감내역을 조회합니다.(날짜 검색 가능)")
    public ApiPageRes<ReceiveBalanceRes> findReceiveBalances(@RequestParam(required = false, defaultValue = "1") int page,
                                                             @RequestParam(required = false,defaultValue = "10")int size,
                                                             @RequestParam(required = false) String startDate,
                                                             @RequestParam(required = false) String endDate){
        return new ApiPageRes<>(receiveBalanceService.findBatchBalances(new MyPageReq(page,size).of(),startDate,endDate));
    }

}
