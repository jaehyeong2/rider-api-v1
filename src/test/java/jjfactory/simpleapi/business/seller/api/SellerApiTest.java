package jjfactory.simpleapi.business.seller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.delivery.api.DeliveryApi;
import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.business.seller.dto.res.SellerAccidentRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerDeliveryRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerRiderRes;
import jjfactory.simpleapi.business.seller.service.SellerService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SellerApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class SellerApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    SellerService sellerService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print()).build();
    }

    @Test
    @WithMockUser
    @DisplayName("지정 생성 성공")
    void createSeller() throws Exception {
        //given
        SellerCreate dto = SellerCreate.builder()
                .address(Address.builder()
                        .deliveryAddress1("d1").deliveryAddress2("d2")
                        .pickUpAddress1("p1").pickUpAddress2("p2")
                        .build())
                .name("sellerC")
                .bizNum("1234567891")
                .chargeRate(1600)
                .tell("0311231234")
                .build();

        String parsed = mapper.writeValueAsString(dto);

        //stub
        given(sellerService.createSeller(any())).willReturn(1L);

        //expected
        mockMvc.perform(post("/seller").with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("지정 수정 성공")
    void modifySeller() throws Exception {
        //given
        SellerModify dto = SellerModify.builder()
                .address(Address.builder()
                        .deliveryAddress1("d1").deliveryAddress2("d2")
                        .pickUpAddress1("p1").pickUpAddress2("p2")
                        .build())
                .name("sellerC")
                .tell("0311231234")
                .build();

        String parsed = mapper.writeValueAsString(dto);

        //stub
        given(sellerService.modifySellerInfo(anyString(),any())).willReturn(1L);

        //expected
        mockMvc.perform(patch("/seller").with(csrf())
                        .content(parsed)
                        .header("sellerCode","testSellerCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("지점 보상금 조회")
    void findSellerTotalCompensation() throws Exception {
        //stub
        given(sellerService.findTotalCompensation(anyString(),anyString()))
                .willReturn(10000);


        //expected
        mockMvc.perform(get("/seller/compensation")
                        .param("sellerName","testSeller")
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(10000));
    }

    @Test
    @WithMockUser
    @DisplayName("지점 사고 조회 페이징")
    void findSellerAccidents() throws Exception {
        //given
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("sellerName","testSeller");
        map.add("page", String.valueOf(1));
        map.add("size",String.valueOf(10));

        SellerAccidentRes res1 = SellerAccidentRes.builder()
                .compensation(10000)
                .deliveryId("delivery23")
                .build();

        SellerAccidentRes res2 = SellerAccidentRes.builder()
                .compensation(20000)
                .deliveryId("delivery23")
                .build();

        PageRequest pageable = new MyPageReq(1, 10).of();

        List<SellerAccidentRes> list = List.of(res1, res2);
        PageImpl<SellerAccidentRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<SellerAccidentRes> pagingRes = new PagingRes<>(page);

        //stub
        given(sellerService.findSellerAccident(any(Pageable.class),anyString(),anyString()))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/seller/accidents")
                        .param("sellerName","testSeller")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].compensation").value(10000));
    }

    @Test
    @WithMockUser
    @DisplayName("지점 하위 라이더 조회 페이징")
    void findRidersInSeller() throws Exception {
        //given
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("sellerName","testSeller");
        map.add("page", String.valueOf(1));
        map.add("size",String.valueOf(10));

        SellerRiderRes res1 = SellerRiderRes.builder()
                .driverId("dd1234")
                .riderName("이재형")
                .build();

        SellerRiderRes res2 = SellerRiderRes.builder()
                .driverId("dd1234")
                .riderName("김첨지")
                .build();

        PageRequest pageable = new MyPageReq(1, 10).of();

        List<SellerRiderRes> list = List.of(res1, res2);
        PageImpl<SellerRiderRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<SellerRiderRes> pagingRes = new PagingRes<>(page);

        //stub
        given(sellerService.findRidersInSeller(any(Pageable.class),anyString(),anyString()))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/seller/riders")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].riderName").value("이재형"));
    }

    @Test
    @WithMockUser
    @DisplayName("지점 운행조회 페이징")
    void findDeliveriesBySellerCode() throws Exception {
        //given
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("sellerName","testSeller");
        map.add("page", String.valueOf(1));
        map.add("size",String.valueOf(10));
        map.add("startDate","2022-10-06 10:23:00");
        map.add("endDate","2022-10-06 23:59:59");

        PageRequest pageable = new MyPageReq(1, 10).of();

        SellerDeliveryRes delivery1 = SellerDeliveryRes.builder()
                .deliveryId("delivery1")
                .build();

        SellerDeliveryRes delivery2 = SellerDeliveryRes.builder()
                .deliveryId("delivery1")
                .build();

        List<SellerDeliveryRes> list = List.of(delivery1, delivery2);
        PageImpl<SellerDeliveryRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<SellerDeliveryRes> pagingRes = new PagingRes<>(page);

        //stub
        given(sellerService.findSellerDeliveries(any(Pageable.class),anyString(),anyString(),anyString(),anyString()))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/seller/deliveries")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].deliveryId").value("delivery1"));
    }
}