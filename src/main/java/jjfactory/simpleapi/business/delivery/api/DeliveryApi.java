package jjfactory.simpleapi.business.delivery.api;

import io.swagger.annotations.ApiOperation;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.res.DeliveryEndRes;
import jjfactory.simpleapi.business.delivery.service.DeliveryService;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.ApiListRes;
import jjfactory.simpleapi.global.dto.res.ApiPageRes;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/delivery")
@RequiredArgsConstructor
@RestController
public class DeliveryApi {
    private final DeliveryService deliveryService;
    @PostMapping("/start")
    @ApiOperation(value = "운행시작", notes = "보험사 서버로 라이더가 운행을 시작했다는 정보를 전송합니다.")
    public ApiRes<String> deliveryStart(@RequestBody DeliveryCreate dto,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(deliveryService.deliveryStart(dto,principalDetails.getRider()));
    }

    @PostMapping("/end")
    @ApiOperation(value = "운행종료", notes = "보험사 서버로 라이더가 운행을 종료했다는 정보를 전송합니다.")
    public ApiRes<DeliveryEndRes> deliveryEnd(@RequestBody DeliveryEndReq dto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(deliveryService.deliveryEnd(dto,principalDetails.getRider()));
    }

    @GetMapping
    @ApiOperation(value = "운행 내역 조회", notes = "라이더 본인의 운행 내역을 조회합니다.(날짜검색가능)")
    public ApiPageRes<DeliveryRes> findMyDeliveries(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @RequestParam(required = false, defaultValue = "1") int page,
                                                    @RequestParam(required = false,defaultValue = "10")int size,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate){
        return new ApiPageRes<>(deliveryService.findMyDeliveries(new MyPageReq(page,size).of(),
                startDate,
                endDate,
                principalDetails.getRider()));
    }

    @GetMapping("/{driverId}")
    @ApiOperation(value = "3일치 운행 조회", notes = "보험사 서버에서 driverId값을 수신하면, 해당라이더의 3일치 운행내역을 리턴합니다.")
    public ApiListRes<DeliveryRes> findDeliveriesIn3Days(@PathVariable String driverId){
        return new ApiListRes<>(deliveryService.find3DaysDeliveriesByDriverId(driverId));
    }
}
