package jjfactory.simpleapi.business.accident.api;

import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.accident.service.AccidentService;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.ApiPageRes;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accidents")
@RequiredArgsConstructor
@RestController
public class AccidentApi {
    private final AccidentService accidentService;

    @GetMapping
    public ApiPageRes<Accident> findMyAccidents(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestParam(required = false, defaultValue = "1")int page,
                                                @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(accidentService.findMyAccidentsByPhone(new MyPageReq(page,size).of(),principalDetails.getRider()));
    }

    @PostMapping
    public ApiRes<Long> createAccident(@RequestBody AccidentCreate dto){
        return new ApiRes<>(accidentService.createAccident(dto));
    }

}
