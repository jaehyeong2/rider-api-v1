package jjfactory.simpleapi.business.rider.service;


import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.res.TokenRes;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.global.config.auth.TokenProvider;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RiderRepository riderRepository;

    @Transactional(readOnly = true)
    public TokenRes login(LoginReq dto, HttpServletResponse res){
        Rider rider = riderRepository.findByLoginId(dto.getLoginId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });

        matchPassword(dto.getPassword(),rider.getPassword());
        String token = createToken(rider);
        res.setHeader("Authorization",token);

        return new TokenRes(token);
    }

    //jwt 토큰생성
    public String createToken(Rider rider){
        return tokenProvider.createToken(String.valueOf(rider.getId()),rider.getRoles());
    }

    public void matchPassword(String reqPassword, String userPassword){
        boolean matches = passwordEncoder.matches(reqPassword, userPassword);

        if(!matches){
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
