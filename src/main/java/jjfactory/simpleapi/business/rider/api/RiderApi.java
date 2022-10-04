package jjfactory.simpleapi.business.rider.api;


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
    public ApiRes<RiderInsuranceStatusRes> getInsuranceStatus(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(riderService.getRiderInsuranceStatus(principalDetails.getRider()));
    }

    @PostMapping("/withdraw")
    public ApiRes<String> withdraw(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ApiRes<>(riderService.withdraw(principalDetails.getRider()));
    }

    @PostMapping("/login")
    public ApiRes<TokenRes> login(@Valid @RequestBody LoginReq dto){
        return new ApiRes(authService.login(dto));
    }

    @PostMapping("/signup")
    public ApiRes<Long> signUp(@Valid @RequestPart RiderCreate dto,
                               @RequestPart(required = false) MultipartFile image){
        return new ApiRes(authService.signUp(dto,image));
    }
}
