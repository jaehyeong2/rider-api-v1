package jjfactory.simpleapi.business.insurance.service;

import jjfactory.simpleapi.business.insurance.dto.res.EndorsementCancelRes;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementRes;
import jjfactory.simpleapi.business.insurance.dto.res.UnderWritingRes;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.insurance.repository.RejectRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.repository.RiderRepositorySupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceResultServiceTest {
    @InjectMocks
    private InsuranceResultService insuranceResultService;
    @Mock
    private InsuranceHistoryRepository insuranceHistoryRepository;
    @Mock
    private RiderRepositorySupport riderRepositorySupport;
    @Mock
    private RejectRepository rejectRepository;

    @Test
    @DisplayName("언더라이팅 결과 수신 성공")
    void underWritingResult() {
        //given
        UnderWritingRes res = UnderWritingRes.builder()
                .driverId("DD1234")
                .bikeNumber("서울자1234")
                .result("accepted")
                .build();

        UnderWritingRes res2 = UnderWritingRes.builder()
                .driverId("DD2222")
                .bikeNumber("부산아1234")
                .result("rejected")
                .build();

        List<UnderWritingRes> list = List.of(res, res2);
        List<UnderWritingRes> rejected = list.stream()
                .filter(e -> !e.getResult().equals("accepted"))
                .collect(Collectors.toList());

        //stub
        when(insuranceHistoryRepository.count()).thenReturn((long) list.size());
        when(rejectRepository.count()).thenReturn((long) rejected.size());

        //when
        int count = insuranceResultService.underWritingResult(list).getCount();
        long historyCount = insuranceHistoryRepository.count();
        long rejectCount = rejectRepository.count();

        //then
        assertThat(count).isEqualTo(2);
        assertThat(historyCount).isEqualTo(2L);
        assertThat(rejectCount).isEqualTo(1L);
    }

    @Test
    @DisplayName("기명 결과 수신 성공")
    void endorsementResult() {
        //given
        EndorsementRes res = EndorsementRes.builder()
                .driverId("DD1234")
                .bikeNumber("서울자1234")
                .result("endorsed")
                .build();

        EndorsementRes res2 = EndorsementRes.builder()
                .driverId("DD2222")
                .bikeNumber("부산아1234")
                .result("rejected")
                .build();

        List<EndorsementRes> list = List.of(res, res2);
        List<EndorsementRes> rejected = list.stream()
                .filter(e -> !e.getResult().equals("endorsed"))
                .collect(Collectors.toList());

        //stub
        when(insuranceHistoryRepository.count()).thenReturn((long) list.size());
        when(rejectRepository.count()).thenReturn((long) rejected.size());

        //when
        int count = insuranceResultService.endorsementResult(list).getCount();
        long historyCount = insuranceHistoryRepository.count();
        long rejectCount = rejectRepository.count();

        //then
        assertThat(count).isEqualTo(2);
        assertThat(historyCount).isEqualTo(2L);
        assertThat(rejectCount).isEqualTo(1L);
    }

    @Test
    @DisplayName("기명취소 결과 수신 성공")
    void cancelResult() {
        //given
        EndorsementCancelRes res = EndorsementCancelRes.builder()
                .driverId("DD1234")
                .bikeNumber("서울자1234")
                .result("canceled")
                .build();

        EndorsementCancelRes res2 = EndorsementCancelRes.builder()
                .driverId("DD2222")
                .bikeNumber("부산아1234")
                .result("rejected")
                .build();

        List<EndorsementCancelRes> list = List.of(res, res2);
        List<EndorsementCancelRes> rejected = list.stream()
                .filter(e -> !e.getResult().equals("canceled"))
                .collect(Collectors.toList());

        //stub
        when(insuranceHistoryRepository.count()).thenReturn((long) list.size());
        when(rejectRepository.count()).thenReturn((long) rejected.size());

        //when
        int count = insuranceResultService.cancelResult(list).getCount();
        long historyCount = insuranceHistoryRepository.count();
        long rejectCount = rejectRepository.count();

        //then
        assertThat(count).isEqualTo(2);
        assertThat(historyCount).isEqualTo(2L);
        assertThat(rejectCount).isEqualTo(1L);
    }
}