package jjfactory.simpleapi.business.rider.service;

import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.domain.Role;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.dto.res.TokenRes;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.repository.SellerRepository;
import jjfactory.simpleapi.global.config.auth.TokenProvider;
import jjfactory.simpleapi.global.uitls.enc.AES_Encryption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@EnableConfigurationProperties
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private RiderRepository riderRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private SellerRepository sellerRepository;

    private AES_Encryption encryption;

    private static final String testToken = "sadasdasdasdas";

    @Test
    @DisplayName("회원가입 성공")
    void signUp() throws Exception {
        //given
        RiderCreate req = RiderCreate.builder().loginId("wogud2").name("이재형")
                .password("1234")
                .ssn("9608221111111")
                .phone("01012341234")
                .build();

        Rider rider = Rider.builder().name("이재형")
                .loginId("wogud2")
                .password("1234")
                .build();

        Seller seller = Seller.builder()
                .name("운영사a")
                .sellerCode("1234123")
                .build();

        //stub
        when(sellerRepository.findBySellerCode(any())).thenReturn(seller);
        when(riderRepository.save(any())).thenReturn(rider);

        //when
        Long res = authService.signUp(req, null);

        //then
        assertThat(res).isEqualTo(rider.getId());
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {
        //given
        Rider rider = Rider.builder().name("이재형")
                .loginId("wogud2")
                .password("1234")
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();

        riderRepository.save(rider);

        LoginReq req = LoginReq.builder().loginId("wogud2").password("1234").build();

        //stub
        when(riderRepository.findByLoginId(rider.getLoginId())).thenReturn(Optional.of(rider));
        when(passwordEncoder.matches(rider.getPassword(),req.getPassword())).thenReturn(true);
        when(tokenProvider.createToken("wogud2", Collections.singletonList(Role.ROLE_USER))).thenReturn(testToken);

        //when
        TokenRes res = authService.login(req);

        //then
        assertThat(res.getJwtToken()).isEqualTo(testToken);
    }
}