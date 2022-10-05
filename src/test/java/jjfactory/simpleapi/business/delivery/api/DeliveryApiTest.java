package jjfactory.simpleapi.business.delivery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.accident.api.AccidentApi;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.res.DeliveryEndRes;
import jjfactory.simpleapi.business.delivery.service.DeliveryService;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        //given
        DeliveryCreate dto = DeliveryCreate.builder()
                .driverId("GG1234")
                .receiveId("20220930152803")
                .build();

        String parsed = mapper.writeValueAsString(dto);

        //stub
        given(deliveryService.deliveryStart(any(),any(Rider.class)))
                .willReturn("ok");

        //expected
        mockMvc.perform(post("/delivery/start").with(user(principalDetails)).with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));
    }

    @Test
    @DisplayName("운행종료 요청성공")
    void deliveryEnd() throws Exception {
        //given
        DeliveryEndReq dto = DeliveryEndReq.builder()
                .deliveryId("dd1234")
                .endTime("2022-10-04 14:25:59")
                .build();

        DeliveryEndRes res = DeliveryEndRes.builder()
                .balance(10000)
                .endDate(dto.getEndTime()).build();

        String parsed = mapper.writeValueAsString(dto);

        //stub
        given(deliveryService.deliveryEnd(any(),any(Rider.class)))
                .willReturn(res);

        //expected
        mockMvc.perform(post("/delivery/end").with(user(principalDetails)).with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.balance").value(10000));
    }

    @Test
    @DisplayName("운행 조회 성공")
    void findMyDeliveries() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        DeliveryRes de11 = DeliveryRes.builder()
                .balance(1000)
                .deliveryId("de11")
                .build();

        DeliveryRes de12 = DeliveryRes.builder()
                .balance(2000)
                .deliveryId("de12")
                .build();

        List<DeliveryRes> list = List.of(de11, de12);
        PageImpl<DeliveryRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<DeliveryRes> pagingRes = new PagingRes<>(page);

        //stub
        given(deliveryService.findMyDeliveries(any(Pageable.class),anyString(),anyString(),any(Rider.class)))
                .willReturn(pagingRes);


        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("startDate","~");
        map.add("endDate","~");
        map.add("page","1");
        map.add("size","10");


        //expected
        mockMvc.perform(get("/delivery").with(user(principalDetails)).with(csrf())
                        .params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].balance").value(1000));
    }

    @Test
    @WithMockUser
    @DisplayName("3일치 운행 조회 성공")
    void findDeliveriesIn3Days() throws Exception {
        //given
        DeliveryRes de11 = DeliveryRes.builder()
                .balance(1000)
                .deliveryId("de11")
                .build();

        DeliveryRes de12 = DeliveryRes.builder()
                .balance(2000)
                .deliveryId("de12")
                .build();

        List<DeliveryRes> list = List.of(de11, de12);

        //stub
        given(deliveryService.find3DaysDeliveriesByDriverId(anyString()))
                .willReturn(list);


        //expected
        mockMvc.perform(get("/delivery"+"/1234").with(csrf())
                        .param("driverId",principalDetails.getRider().getDriverId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].deliveryId").value("de11"))
                .andExpect(jsonPath("$.result[0].balance").value(1000));
    }
}