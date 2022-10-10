package jjfactory.simpleapi.business.seller.api;


import io.swagger.annotations.ApiOperation;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.business.seller.dto.res.SellerAccidentRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerDeliveryRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerRiderRes;
import jjfactory.simpleapi.business.seller.service.SellerService;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.ApiPageRes;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/seller")
@RequiredArgsConstructor
@RestController
public class SellerApi {
    private final SellerService sellerService;

    @GetMapping("/compensation")
    @ApiOperation(value = "지점 총 보상금 조회", notes = "지점 하위 라이더들에게 발생한 사고의 보상금 총합 리턴")
    public ApiRes<Integer> findSellerTotalCompensation(@RequestParam String sellerName,
                                               @RequestHeader String sellerCode){
        return new ApiRes<>(sellerService.findTotalCompensation(sellerCode,sellerName));
    }

    @GetMapping("/accidents")
    @ApiOperation(value = "지점 사고 조회", notes = "지점 하위 라이더들에게 발생한 사고 조회")
    public ApiPageRes<SellerAccidentRes> findSellerAccidents(@RequestParam String sellerName,
                                                         @RequestHeader String sellerCode,
                                                         @RequestParam(required = false, defaultValue = "1")int page,
                                                         @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(sellerService.findSellerAccident(new MyPageReq(page,size).of(),sellerCode,sellerName));
    }

    @GetMapping("/riders")
    @ApiOperation(value = "지점 하위 라이더 조회", notes = "지점 하위 라이더의 정보 조회")
    public ApiPageRes<SellerRiderRes> findRidersInSeller(@RequestParam String sellerName,
                                                         @RequestHeader String sellerCode,
                                                         @RequestParam(required = false, defaultValue = "1")int page,
                                                         @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(sellerService.findRidersInSeller(new MyPageReq(page,size).of(),sellerCode,sellerName));
    }

    @GetMapping("/deliveries")
    @ApiOperation(value = "지점 하위 라이더 운행조회", notes = "지점 하위 라이더 운행 조회. 날짜검색 가능")
    public ApiPageRes<SellerDeliveryRes> findDeliveriesBySellerName(@RequestParam String sellerName,
                                                                    @RequestHeader String sellerCode,
                                                                    @RequestParam(required = false, defaultValue = "1")int page,
                                                                    @RequestParam(required = false, defaultValue = "10")int size,
                                                                    @RequestParam(required = false) String startDate,
                                                                    @RequestParam(required = false) String endDate){
        return new ApiPageRes<>(sellerService.findSellerDeliveries(new MyPageReq(page,size).of(),startDate,endDate,sellerCode,sellerName));
    }

    @PostMapping
    @ApiOperation(value = "지점 생성")
    public ApiRes<Long> createSeller(@RequestBody SellerCreate sellerCreate){
        return new ApiRes<>(sellerService.createSeller(sellerCreate));
    }

    @PatchMapping
    @ApiOperation(value = "지점 정보 수정")
    public ApiRes<Long> modifySeller(@RequestBody SellerModify sellerModify,
                                     @RequestHeader String sellerCode){
        return new ApiRes<>(sellerService.modifySellerInfo(sellerCode,sellerModify));
    }
}
