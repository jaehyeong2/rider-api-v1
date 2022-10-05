package jjfactory.simpleapi.business.accident.api;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.TestPrincipalDetailsService;
import jjfactory.simpleapi.business.WithAuthUser;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.res.AccidentRes;
import jjfactory.simpleapi.business.accident.service.AccidentService;
import jjfactory.simpleapi.business.balance.api.ReceiveBalanceApi;
import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.domain.Role;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.config.auth.PrincipalDetails;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.MockServerConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccidentApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class AccidentApiTest {
    @Autowired
    private WebApplicationContext context;
    private SecurityContext securityContext;
    MockMvc mockMvc;
    @MockBean
    private AccidentService accidentService;
    private final TestPrincipalDetailsService testUserDetailsService = new TestPrincipalDetailsService();
    private PrincipalDetails principalDetails;
    Accident accident;
    Delivery delivery;
    Rider rider;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print()).build();

        principalDetails = (PrincipalDetails) testUserDetailsService.loadUserByUsername(TestPrincipalDetailsService.USERNAME);
        setUpEntity();
    }

    //    @WithUserDetails(value = "wogud222" ,setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @WithAuthUser(loginId = "wogud222", role = "user") 둘다 안됨
    @Test
    @DisplayName("내 사고 내역 조회 성공")
    void findMyAccidents() throws Exception {
        //given
        AccidentRes res = new AccidentRes(accident, delivery);
        List<AccidentRes> list = Collections.singletonList(res);

        PageRequest pageable = new MyPageReq(1, 10).of();

        Page<AccidentRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<AccidentRes> pagingRes = new PagingRes<>(page);

        //stub
        given(accidentService.findMyAccidentsByPhone(pageable,principalDetails.getRider())).willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/accidents").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].compensation").value(175000));
    }

    private void setUpEntity() {
        rider = Rider.builder()
                .name("이재형")
                .driverId("gg1234")
                .loginId("wogud222")
                .phone("01012341234")
                .build();

        delivery = Delivery.builder()
                .rider(rider)
                .address(Address.builder().build())
                .build();

        accident = Accident.builder()
                .delivery(delivery)
                .compensation(175000)
                .accidentTime(LocalDateTime.of(2022,9,27,14,25,0))
                .build();
    }

}