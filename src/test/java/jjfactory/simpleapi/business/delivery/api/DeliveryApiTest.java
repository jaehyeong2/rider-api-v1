package jjfactory.simpleapi.business.delivery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.accident.api.AccidentApi;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.service.DeliveryService;
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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeliveryApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class DeliveryApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    DeliveryService deliveryService;
    @Autowired
    private WebApplicationContext context;
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
    @DisplayName("운행시작 요청성공")
    void deliveryStart() throws Exception {
        DeliveryCreate dto = DeliveryCreate.builder()
                .driverId("GG1234")
                .receiveId("20220930152803")
                .build();

        String parsed = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/delivery/start").with(user(principalDetails)).with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("운행종료 요청성공")
    void deliveryEnd() throws Exception {
        DeliveryEndReq dto = DeliveryEndReq.builder()
                .deliveryId("dd1234")
                .endTime("2022-10-04 14:25:59")
                .build();

        String parsed = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/delivery/end").with(user(principalDetails)).with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("운행 조회 성공")
    void findMyDeliveries() throws Exception {
        mockMvc.perform(get("/delivery").with(user(principalDetails)).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("3일치 운행 조회 성공")
    void findDeliveriesIn3Days() throws Exception {
        mockMvc.perform(get("/delivery"+"/1234").with(csrf()))
                .andExpect(status().isOk());
    }
}