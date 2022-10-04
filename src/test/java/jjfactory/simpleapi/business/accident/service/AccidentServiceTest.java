package jjfactory.simpleapi.business.accident.service;

import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.accident.dto.res.AccidentRes;
import jjfactory.simpleapi.business.accident.repository.AccidentRepository;
import jjfactory.simpleapi.business.accident.repository.AccidentRepositorySupport;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccidentServiceTest {
    @InjectMocks
    AccidentService accidentService;
    @Mock
    DeliveryRepository deliveryRepository;
    @Mock
    AccidentRepositorySupport accidentRepositorySupport;
    @Spy
    AccidentRepository accidentRepository;

    Rider rider;
    Delivery delivery;

    @BeforeEach
    void init(){
        Seller seller = Seller.builder()
                .sellerCode("seller12342")
                .name("sellerA")
                .build();

        rider = Rider.builder()
                .seller(seller)
                .name("이재형")
                .driverId("gg1234")
                .phone("01012341234")
                .build();

        delivery = Delivery.builder()
                .rider(rider)
                .deliveryId("delivery2")
                .build();

        Accident accident = Accident.builder()
                .delivery(delivery)
                .build();
    }

    @Test
    @DisplayName("사고 접수 성공")
    void createAccident() {
        //given
        AccidentCreate dto = AccidentCreate.builder()
                .deliveryId("delivery2")
                .claimTime("2022-10-04 12:05:47")
                .accident_time("2022-10-04 12:03:00")
                .build();

        //stub
        when(deliveryRepository.findByDeliveryId(any())).thenReturn(Optional.ofNullable(delivery));

        //when
        accidentService.createAccident(dto);

    }

    @Test
    @DisplayName("사고 조회 성공")
    void findMyAccidentsByPhone() {
        //given
        PageRequest pageRequest = new MyPageReq(1, 10).of();

        AccidentRes res = AccidentRes.builder()
                .deliveryId(delivery.getDeliveryId())
                .build();

        List<AccidentRes> list = List.of(res);

        //stub
        when(accidentRepositorySupport.findAccidentsByPhone(pageRequest,"01012341234"))
                .thenReturn(new PageImpl<>(list,pageRequest,list.size()));

        //when
        PagingRes<Accident> result = accidentService.findMyAccidentsByPhone(pageRequest, rider);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("보상금 조회 성공")
    void findTotalCompensation() {
        //when
        Integer totalCompensation = accidentService.findTotalCompensation("seller12342");

        //then
        assertThat(totalCompensation).isNotNull();
    }
}