package jjfactory.simpleapi.business.rider.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.business.rider.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class AuthApiTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    AuthService authService;
    @Autowired
    RiderRepository riderRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print()).build();
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
        mockMvc.perform(post("/auth/login").content(mapper.writeValueAsString(loginReq))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.jwtToken").isNotEmpty());

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
        mockMvc.perform(multipart("/auth/signup")
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