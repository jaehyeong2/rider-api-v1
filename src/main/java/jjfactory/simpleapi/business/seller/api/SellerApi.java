package jjfactory.simpleapi.business.seller.api;


import jjfactory.simpleapi.business.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sellers")
@RequiredArgsConstructor
@RestController
public class SellerApi {
    private final SellerService sellerService;
}
