package jjfactory.simpleapi.business.seller.api;


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
    public ApiRes<Integer> findSellerTotalCompensation(@RequestParam String sellerName,
                                               @RequestHeader String sellerCode){
        return new ApiRes<>(sellerService.findTotalCompensation(sellerCode,sellerName));
    }

    @GetMapping("/accidents")
    public ApiPageRes<SellerAccidentRes> findSellerAccidents(@RequestParam String sellerName,
                                                         @RequestHeader String sellerCode,
                                                         @RequestParam(required = false, defaultValue = "1")int page,
                                                         @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(sellerService.findSellerAccident(new MyPageReq(page,size).of(),sellerCode,sellerName));
    }

    @GetMapping("/riders")
    public ApiPageRes<SellerRiderRes> findRidersInSeller(@RequestParam String sellerName,
                                                         @RequestHeader String sellerCode,
                                                         @RequestParam(required = false, defaultValue = "1")int page,
                                                         @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(sellerService.findRidersInSeller(new MyPageReq(page,size).of(),sellerCode,sellerName));
    }

    @GetMapping("/deliveries")
    public ApiPageRes<SellerDeliveryRes> findDeliveriesBySellerCode(@RequestParam String sellerName,
                                                                    @RequestHeader String sellerCode,
                                                                    @RequestParam(required = false, defaultValue = "1")int page,
                                                                    @RequestParam(required = false, defaultValue = "10")int size,
                                                                    @RequestParam(required = false) String startDate,
                                                                    @RequestParam(required = false) String endDate){
        return new ApiPageRes<>(sellerService.findSellerDeliveries(new MyPageReq(page,size).of(),startDate,endDate,sellerCode,sellerName));
    }

    @PostMapping
    public ApiRes<Long> createSeller(@RequestBody SellerCreate sellerCreate){
        return new ApiRes<>(sellerService.createSeller(sellerCreate));
    }

    @PatchMapping
    public ApiRes<Long> modifySeller(@RequestBody SellerModify sellerModify,
                                     @RequestHeader String sellerCode){
        return new ApiRes<>(sellerService.modifySellerInfo(sellerCode,sellerModify));
    }
}
