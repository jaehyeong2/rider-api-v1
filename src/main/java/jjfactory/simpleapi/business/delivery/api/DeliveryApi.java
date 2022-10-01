package jjfactory.simpleapi.business.delivery.api;

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
    public ApiRes<String> deliveryStart(@RequestBody DeliveryCreate dto,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(deliveryService.deliveryStart(dto,principalDetails.getRider()));
    }

    @PostMapping("/end")
    public ApiRes<DeliveryEndRes> deliveryEnd(@RequestBody DeliveryEndReq dto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(deliveryService.deliveryEnd(dto,principalDetails.getRider()));
    }

    @GetMapping
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

    @GetMapping
    public ApiListRes<DeliveryRes> findDeliveriesIn3Days(@RequestParam String driverId){
        return new ApiListRes<>(deliveryService.find3DaysDeliveriesByDriverId(driverId));
    }
}
