package jjfactory.simpleapi.business.rider.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.service.AuthService;
import jjfactory.simpleapi.business.rider.service.RiderService;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RiderApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class RiderApiTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    AuthService authService;
    @MockBean
    RiderService riderService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private final TestPrincipalDetailsService testUserDetailsService = new TestPrincipalDetailsService();
    private PrincipalDetails principalDetails;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print()).build();

        principalDetails = (PrincipalDetails) testUserDetailsService.loadUserByUsername(TestPrincipalDetailsService.USERNAME);
    }

    @Test
    @DisplayName("보험 적용상태 조회")
    void getInsuranceStatus() throws Exception {
        //expected
        mockMvc.perform(get("/riders/insurance").with(user(principalDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void withdraw() throws Exception {
        //expected
        mockMvc.perform(post("/riders/withdraw").with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        //given
        loginSetUp();

        LoginReq loginReq = LoginReq.builder()
                .loginId("wogud222")
                .password("1234").build();

        //expected
        mockMvc.perform(post("/riders/login").content(mapper.writeValueAsString(loginReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp() throws Exception {
        //given
        RiderCreate riderCreate = RiderCreate.builder()
                .name("이재형")
                .phone("01092491234")
                .loginId("wogud222")
                .bikeNumber("부산가1234")
                .gender(1)
                .password("!234")
                .sellerCode("테스트테스트얍")
                .ssn("9608221111111")
                .region("01")
                .build();

        String createJson = mapper.writeValueAsString(riderCreate);
        MockMultipartFile dto =
                new MockMultipartFile("dto", "dto",
                        "application/json",
                        createJson.getBytes(StandardCharsets.UTF_8));

        MockMultipartFile image =
                new MockMultipartFile("file", "test.txt",
                        "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );

        //expected
        mockMvc.perform(multipart("/riders/signup")
                        .file(image)
                        .file(dto)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andReturn();

    }

    private void loginSetUp() {
        RiderCreate riderCreate = RiderCreate.builder()
                .name("이재형")
                .phone("01092491234")
                .loginId("wogud222")
                .bikeNumber("부산가1234")
                .gender(1)
                .password("1234")
                .sellerCode("테스트테스트얍")
                .ssn("9608221111111")
                .region("01")
                .build();

        authService.signUp(riderCreate,null);
    }
}