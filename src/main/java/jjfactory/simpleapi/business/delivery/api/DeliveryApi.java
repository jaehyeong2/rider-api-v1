package jjfactory.simpleapi.business.delivery.api;

import jjfactory.simpleapi.business.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/delivery")
@RequiredArgsConstructor
@RestController
public class DeliveryApi {
    private final DeliveryService deliveryService;
}
