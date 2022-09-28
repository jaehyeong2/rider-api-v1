package jjfactory.simpleapi.business.insurance.service;

import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.insurance.repository.RejectRepository;
import jjfactory.simpleapi.business.rider.repository.RiderRepositorySupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.junit.jupiter.api.Assertions.*;

@EnableConfigurationProperties
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
    void underWritingResult() {
    }

    @Test
    void endorsementResult() {
    }

    @Test
    void cancelResult() {
    }
}