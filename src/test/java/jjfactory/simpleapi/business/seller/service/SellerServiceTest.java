package jjfactory.simpleapi.business.seller.service;

import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.repository.AccidentRepositorySupport;
import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.delivery.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.repository.RiderRepositorySupport;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.business.seller.dto.res.SellerAccidentRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerDeliveryRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerRiderRes;
import jjfactory.simpleapi.business.seller.repository.SellerRepository;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {
    @InjectMocks
    private SellerService sellerService;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private AccidentRepositorySupport accidentRepositorySupport;
    @Mock private DeliveryRepositorySupport deliveryRepositorySupport;
    @Mock private RiderRepositorySupport riderRepositorySupport;

    Seller seller;
    Rider rider;
    Delivery delivery;
    Accident accident;
    BalanceHistory balanceHistory;

    @BeforeEach
    void init(){
        seller = Seller.builder()
                .sellerCode("seller12342")
                .name("sellerA")
                .build();

        rider = Rider.builder()
                .seller(seller)
                .name("이재형")
                .driverId("gg1234")
                .phone("01012341234")
                .createDate(LocalDateTime.now().minusDays(5))
                .build();

        delivery = Delivery.builder()
                .rider(rider)
                .address(Address.builder().build())
                .deliveryId("delivery2")
                .appointTime(LocalDateTime.of(2022,10,3,11,25,0))
                .completeTime(LocalDateTime.of(2022,10,3,11,55,0))
                .build();

        accident = Accident.builder()
                .delivery(delivery)
                .accidentTime(LocalDateTime.now().minusDays(1L))
                .build();

        balanceHistory = BalanceHistory.builder()
                .balance(1000)
                .delivery(delivery)
                .build();
    }

    @Test
    @DisplayName("지점 등록 성공")
    void createSeller() {
        //given
        SellerCreate create = SellerCreate.builder()
                .name("sellerB")
                .bizNum("1234567891")
                .address(Address.builder().build())
                .chargeRate(1600)
                .build();

        //when
        sellerService.createSeller(create);
    }

    @Test
    @DisplayName("지점 코드 안맞으면 exception")
    void modifySellerInfoFailed() {
        //given
        SellerModify modify = SellerModify.builder()
                .name("지점3")
                .build();

        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);

        //expected
        assertThatThrownBy(() -> {
            sellerService.modifySellerInfo("seller123", modify);
        }).isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("지점 수정 성공")
    void modifySellerInfo() {
        //given
        SellerModify modify = SellerModify.builder()
                .address(Address.builder()
                        .deliveryAddress1("test").deliveryAddress2("222-233")
                        .pickUpAddress1("픽업픽업").pickUpAddress2("픽업2!!")
                        .build())
                .tell("0511235566")
                .name("지점3")
                .build();

        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);

        //when
        Long result = sellerService.modifySellerInfo("seller12342", modify);

        //then
        assertThat(result).isEqualTo(seller.getId());
    }

    @Test
    @DisplayName("보상금 조회 성공")
    void findTotalCompensation() {
        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);

        //when
        Integer totalCompensation = sellerService.findTotalCompensation("seller12342","sellerA");

        //then
        assertThat(totalCompensation).isNotNull();
    }

    @Test
    @DisplayName("지점 모든 사고 조회 성공")
    void findSellerAccident() {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        SellerAccidentRes res = new SellerAccidentRes(accident, delivery, rider);
        List<SellerAccidentRes> list = List.of(res);

        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);
        when(accidentRepositorySupport.findAccidentsBySellerName(pageable,"sellerA"))
                .thenReturn(new PageImpl<>(list,pageable,list.size()));

        //when
        PagingRes<SellerAccidentRes> result = sellerService.findSellerAccident(pageable, "seller12342", "sellerA");

        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("지점 모든 운행 조회 성공")
    void findSellerDeliveries() {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();
        String startDate = "2022-10-05 00:00:00";
        String endDate = "2022-10-05 23:59:59";

        SellerDeliveryRes res = new SellerDeliveryRes(delivery, balanceHistory, rider);
        List<SellerDeliveryRes> list = List.of(res);

        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);
        when(deliveryRepositorySupport.findSellerDeliveries(pageable,startDate,endDate,"sellerA"))
                .thenReturn(new PageImpl<>(list,pageable,list.size()));
        //when
        PagingRes<SellerDeliveryRes> result = sellerService
                .findSellerDeliveries(pageable, startDate, endDate, "seller12342", "sellerA");

        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("지점 하위 라이더 조회 성공")
    void findRidersInSeller() {
        //given
        PageRequest pageable = new MyPageReq(1, 10).of();

        SellerRiderRes rider = new SellerRiderRes(this.rider);

        List<SellerRiderRes> list = Collections.singletonList(rider);

        //stub
        when(sellerRepository.findByName(any())).thenReturn(seller);
        when(riderRepositorySupport.findRiderInfoInSeller(pageable,seller.getId()))
                .thenReturn(new PageImpl<>(list,pageable,list.size()));

        //when
        PagingRes<SellerRiderRes> result = sellerService.findRidersInSeller(pageable, "seller12342", "sellerA");

        //then
        SellerRiderRes find = result.getResultList().get(0);
        assertThat(find.getRiderName()).isEqualTo("이재형");
    }

}