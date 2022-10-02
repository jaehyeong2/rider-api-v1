package jjfactory.simpleapi.business.rider.api;


import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.dto.res.TokenRes;
import jjfactory.simpleapi.business.rider.service.AuthService;
import jjfactory.simpleapi.global.dto.res.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthApi {
    private final AuthService authService;

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
