package jjfactory.simpleapi.business.balance.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.balance.dto.req.ReceiveBalanceCreate;
import jjfactory.simpleapi.business.balance.service.ReceiveBalanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        ReceiveBalanceCreate create = ReceiveBalanceCreate.builder()
                .balance(10000)
                .batchDate("2022-09-15 11:22:00")
                .build();

        mockMvc.perform(post("/balance").with(csrf())
                .content(mapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("배치 금액 조회성공")
    void findReceiveBalances() throws Exception {
        mockMvc.perform(get("/balance").with(csrf()))
                .andExpect(status().isOk());
    }
}