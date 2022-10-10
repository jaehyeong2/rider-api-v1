package jjfactory.simpleapi.business.rider.api;


import io.swagger.annotations.ApiOperation;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.dto.res.RiderInsuranceStatusRes;
import jjfactory.simpleapi.business.rider.dto.res.TokenRes;
import jjfactory.simpleapi.business.rider.service.AuthService;
import jjfactory.simpleapi.business.rider.service.RiderService;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/riders")
@RequiredArgsConstructor
@RestController
public class RiderApi {
    private final RiderService riderService;
    private final AuthService authService;

    @GetMapping("/insurance")
    @ApiOperation(value = "보험적용상태 조회", notes = "로그인한 라이더 본인의 보험 적용 여부를 리턴합니다.")
    public ApiRes<RiderInsuranceStatusRes> getInsuranceStatus(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(riderService.getRiderInsuranceStatus(principalDetails.getRider()));
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "앱에서 기명취소요청", notes = "라이더의 기명취소 요청 내역을 저장합니다.")
    public ApiRes<String> withdraw(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(riderService.withdraw(principalDetails.getRider()));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ApiRes<TokenRes> login(@Valid @RequestBody LoginReq dto){
        return new ApiRes(authService.login(dto));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "라이더 정보와 보험증권 이미지를 저장합니다.")
    public ApiRes<Long> signUp(@Valid @RequestPart RiderCreate dto,
                               @RequestPart(required = false) MultipartFile image){
        return new ApiRes(authService.signUp(dto,image));
    }
}
