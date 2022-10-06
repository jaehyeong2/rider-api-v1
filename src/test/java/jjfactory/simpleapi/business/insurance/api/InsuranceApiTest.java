package jjfactory.simpleapi.business.insurance.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jjfactory.simpleapi.business.accident.api.AccidentApi;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementCancelRes;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementRes;
import jjfactory.simpleapi.business.insurance.dto.res.RiderCountRes;
import jjfactory.simpleapi.business.insurance.dto.res.UnderWritingRes;
import jjfactory.simpleapi.business.insurance.service.InsuranceResultService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InsuranceApi.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)}
)
class InsuranceApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    InsuranceResultService insuranceResultService;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print()).build();
    }

    @Test
    @WithMockUser
    @DisplayName("언더라이팅 결과수신")
    void receiveUnderWritingResult() throws Exception {
        //given
        UnderWritingRes result = UnderWritingRes
                .builder()
                .driverId("GG1234")
                .result("accepted")
                .build();

        List<UnderWritingRes> results = List.of(result);

        //stub
        given(insuranceResultService.underWritingResult(any())).willReturn(new RiderCountRes(results.size()));

        //expected
        mockMvc.perform(post("/insurance/underwriting")
                .content(mapper.writeValueAsString(results))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.count").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("기명 결과수신")
    void receiveEndorsementResult() throws Exception {
        //given
        EndorsementRes result = EndorsementRes
                .builder()
                .driverId("GG1234")
                .result("accepted")
                .build();

        List<EndorsementRes> results = List.of(result);

        //stub
        given(insuranceResultService.endorsementResult(any())).willReturn(new RiderCountRes(results.size()));

        //expected
        mockMvc.perform(post("/insurance/endorsement")
                        .content(mapper.writeValueAsString(results))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.count").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("기명취소 결과수신")
    void receiveEndorsementCancelResult() throws Exception {
        //given
        EndorsementCancelRes result = EndorsementCancelRes
                .builder()
                .driverId("GG1234")
                .result("accepted")
                .build();

        List<EndorsementCancelRes> results = List.of(result);

        //stub
        given(insuranceResultService.cancelResult(any())).willReturn(new RiderCountRes(results.size()));

        //expected
        mockMvc.perform(post("/insurance/cancel")
                        .content(mapper.writeValueAsString(results))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.count").value(1L));
    }
}