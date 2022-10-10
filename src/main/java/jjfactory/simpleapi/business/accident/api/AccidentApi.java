package jjfactory.simpleapi.business.accident.api;

import io.swagger.annotations.ApiOperation;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.accident.dto.res.AccidentRes;
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
    @ApiOperation(value = "라이더 사고내역 조회", notes = "라이더 본인의 사고내역을 조회합니다.")
    public ApiPageRes<AccidentRes> findMyAccidents(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                   @RequestParam(required = false, defaultValue = "1")int page,
                                                   @RequestParam(required = false, defaultValue = "10")int size){
        return new ApiPageRes<>(accidentService.findMyAccidentsByPhone(new MyPageReq(page,size).of(),principalDetails.getRider()));
    }

    @PostMapping
    @ApiOperation(value = "사고접수", notes = "수신한 사고 dto를 기반으로 사고 정보를 저장합니다.")
    public ApiRes<Long> createAccident(@RequestBody AccidentCreate dto){
        return new ApiRes<>(accidentService.createAccident(dto));
    }

}
