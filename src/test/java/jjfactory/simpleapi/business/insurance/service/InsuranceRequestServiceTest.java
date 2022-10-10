package jjfactory.simpleapi.business.insurance.service;

import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepositorySupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(InsuranceRequestService.class)
class InsuranceRequestServiceTest {
    @Autowired
    InsuranceRequestService insuranceRequestService;
    @MockBean
    InsuranceHistoryRepositorySupport insuranceHistoryRepositorySupport;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    @DisplayName("언더라이팅 배치 요청 성공")
    void underWritingBatch() {
        //given
        String expectedApiUrl = "http://localhost/rider/underwriting";
        String expectedJsonResponse = "ok";

        mockServer.expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //when
        insuranceRequestService.underWritingBatch();

    }

    @Test
    @DisplayName("기명 배치 요청 성공")
    void endorsementBatch() {
        //given
        String expectedApiUrl = "http://localhost/rider/endorsement";
        String expectedJsonResponse = "ok";

        mockServer
                .expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //when
        insuranceRequestService.endorsementBatch();
    }

    @Test
    @DisplayName("기명취소 배치 요청 성공")
    void withDrawRiderBatch() {
        //given
        String expectedApiUrl = "http://localhost/rider/withdraw";
        String expectedJsonResponse = "ok";

        mockServer
                .expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //when
        insuranceRequestService.withDrawRiderBatch();
    }
}