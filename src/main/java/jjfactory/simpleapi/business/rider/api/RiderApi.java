package jjfactory.simpleapi.business.rider.api;


import jjfactory.simpleapi.business.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/riders")
@RequiredArgsConstructor
@RestController
public class RiderApi {
    private final RiderService riderService;
}
