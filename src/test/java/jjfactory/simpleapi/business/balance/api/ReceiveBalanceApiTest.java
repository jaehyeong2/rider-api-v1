package jjfactory.simpleapi.business.balance.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import jjfactory.simpleapi.business.balance.dto.res.ReceiveBalanceRes;
import jjfactory.simpleapi.business.balance.service.ReceiveBalanceService;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ReceiveBalanceApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class ReceiveBalanceApiTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ReceiveBalanceService balanceService;
    @Autowired
    ObjectMapper mapper;

    @Test
    @WithMockUser
    @DisplayName("배치 금액 수신 성공")
    void receiveBatchBalance() throws Exception {
        //given
        ReceiveBalanceCreate create = ReceiveBalanceCreate.builder()
                .balance(10000)
                .batchDate("2022-09-15 11:22:00")
                .build();

        //stub
        given(balanceService.receiveBatchBalance(any())).willReturn("ok");

        //expected
        mockMvc.perform(post("/balance").with(csrf())
                .content(mapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));
    }

    @Test
    @WithMockUser
    @DisplayName("배치 금액 조회성공")
    void findReceiveBalances() throws Exception {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();
        String startDate = "2022-10-05 00:00:00";
        String endDate = "2022-10-05 23:59:59";

        ReceiveBalanceRes res1 = ReceiveBalanceRes
                .builder().balance(10000).build();

        ReceiveBalanceRes res2 = ReceiveBalanceRes
                .builder().balance(20000).build();

        List<ReceiveBalanceRes> list = List.of(res1, res2);

        PageImpl<ReceiveBalanceRes> page = new PageImpl<>(list, pageable, list.size());
        PagingRes<ReceiveBalanceRes> pagingRes = new PagingRes<>(page);

        //stub
        given(balanceService.findBatchBalances(any(Pageable.class),anyString(),anyString()))
                .willReturn(pagingRes);

        //expected
        mockMvc.perform(get("/balance").with(csrf())
                        .param("page","1")
                        .param("startDate",startDate)
                        .param("endDate",endDate)
                        .param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultList[0].balance").value(10000))
                .andDo(print());
    }
}