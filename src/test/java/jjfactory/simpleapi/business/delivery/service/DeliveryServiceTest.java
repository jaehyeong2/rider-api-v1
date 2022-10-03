package jjfactory.simpleapi.business.delivery.service;

import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryCreate;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.res.DeliveryEndRes;
import jjfactory.simpleapi.business.delivery.repository.BalanceHistoryRepository;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(value = DeliveryService.class)
class DeliveryServiceTest {
    @Autowired
    DeliveryService deliveryService;
    @MockBean
    DeliveryRepository deliveryRepository;
    @MockBean
    BalanceHistoryRepository balanceHistoryRepository;
    @MockBean
    DeliveryRepositorySupport deliveryRepositorySupport;
    @Autowired
    private MockRestServiceServer mockServer;


    @Test
    @DisplayName("오늘 운행 전체 조회")
    void findDeliveryToday() {
        //given
        String expectedApiUrl = "http://localhost/delivery";
        String expectedJsonResponse = "ok";

        mockServer
                .expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //when
        String result = deliveryService.findAllDeliveriesToday();

        //then
        assertThat(result).isEqualTo("ok");
    }

    @Test
    @DisplayName("운행 시작 성공")
    void deliveryStart() {
        //given
        Rider rider = Rider.builder()
                .name("test")
                .driverId("GG1234")
                .build();

        DeliveryCreate req = DeliveryCreate.builder()
                .receiveId("20220928dsd")
                .clientName("맥도날드")
                .requestTime("2022-09-28 14:25:47")
                .appointTime("2022-09-28 14:26:00")
                .build();

        String expectedApiUrl = "http://localhost/delivery/start";
        String expectedJsonResponse = "ok";

        mockServer
                .expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //when
        String result = deliveryService.deliveryStart(req, rider);

        //then
        assertThat(result).isEqualTo("ok");
    }

    @Test
    @DisplayName("운행 종료 성공")
    void deliveryEnd() {
        //given
        Seller seller = Seller.builder()
                .name("sellerA")
                .chargeRate(1800)
                .build();

        Rider rider = Rider.builder()
                .name("test")
                .seller(seller)
                .driverId("GG1234")
                .build();

        Delivery delivery = Delivery
                .builder()
                .rider(rider)
                .pickUpTime(LocalDateTime.of(2022,10,3,14,25,0))
                .build();

        DeliveryEndReq req = DeliveryEndReq.builder()
                .driverId("GG1234")
                .endTime("2022-10-03 14:55:01")
                .build();

        String expectedApiUrl = "http://localhost/delivery/end";
        String expectedJsonResponse = "ok";

        mockServer
                .expect(requestTo(expectedApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        //stub
        when(deliveryRepository.findByDeliveryId(any())).thenReturn(Optional.ofNullable(delivery));

        //when
        DeliveryEndRes res = deliveryService.deliveryEnd(req, rider);

        //then
        assertThat(res.getEndDate()).isEqualTo("2022-10-03 14:55:01");
        assertThat(res.getBalance()).isEqualTo(930);
    }
}