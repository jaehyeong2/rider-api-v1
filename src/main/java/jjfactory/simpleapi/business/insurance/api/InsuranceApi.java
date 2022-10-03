package jjfactory.simpleapi.business.insurance.api;


import io.swagger.annotations.ApiOperation;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementCancelRes;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementRes;
import jjfactory.simpleapi.business.insurance.dto.res.RiderCountRes;
import jjfactory.simpleapi.business.insurance.dto.res.UnderWritingRes;
import jjfactory.simpleapi.business.insurance.service.InsuranceResultService;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/insurance")
@RequiredArgsConstructor
@RestController
public class InsuranceApi {
    private final InsuranceResultService insuranceResultService;

    @PostMapping("/underwriting")
    @ApiOperation(value = "언더라이팅 결과 수신", notes = "라이더들의 언더라이팅 결과를 받아 서버는 카운트를 리턴")
    public ApiRes<RiderCountRes> receiveUnderWritingResult(@RequestBody List<UnderWritingRes> results){
        return new ApiRes(insuranceResultService.underWritingResult(results));
    }

    @PostMapping("/endorsement")
    @ApiOperation(value = "기명요청 결과 수신", notes = "라이더들의 기명 결과를 받아 서버는 카운트를 리턴")
    public ApiRes<RiderCountRes> receiveEndorsementResult(@RequestBody List<EndorsementRes> results){
        return new ApiRes(insuranceResultService.endorsementResult(results));
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "기명취소요청 결과 수신", notes = "라이더들의 기명취소 요청 심사결과를 리스트 형태로 kb로부터 수신합니다. " +
            "고고에프앤디 서버는 카운트를 리턴합니다.")
    public ApiRes<RiderCountRes> receiveEndorsementCancelResult(@RequestBody List<EndorsementCancelRes> results){
        return new ApiRes(insuranceResultService.cancelResult(results));
    }
}
