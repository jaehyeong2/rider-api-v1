package jjfactory.simpleapi.business.seller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.delivery.api.DeliveryApi;
import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.business.seller.service.SellerService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        //expected
        mockMvc.perform(post("/seller").with(csrf())
                        .content(parsed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

        //expected
        mockMvc.perform(patch("/seller").with(csrf())
                        .content(parsed)
                        .header("sellerCode","testSellerCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("지점 보상금 조회")
    void findSellerTotalCompensation() throws Exception {
        //expected
        mockMvc.perform(get("/seller/compensation")
                        .param("sellerName","testSeller")
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk());
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

        //expected
        mockMvc.perform(get("/seller/accidents")
                        .param("sellerName","testSeller")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk());
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

        //expected
        mockMvc.perform(get("/seller/riders")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk());
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

        //expected
        mockMvc.perform(get("/seller/deliveries")
                        .params(map)
                        .header("sellerCode","testSellerCode"))
                .andExpect(status().isOk());
    }
}