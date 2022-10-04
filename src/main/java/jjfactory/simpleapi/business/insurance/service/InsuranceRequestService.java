package jjfactory.simpleapi.business.insurance.service;

import jjfactory.simpleapi.business.delivery.service.DeliveryService;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import jjfactory.simpleapi.business.insurance.dto.req.EndorsementReq;
import jjfactory.simpleapi.business.insurance.dto.req.UnderWritingReq;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepositorySupport;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.insurance.dto.req.RiderWithdrawReq;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.global.config.retrofit.RetrofitApi;
import jjfactory.simpleapi.global.config.retrofit.RetrofitConfig;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class InsuranceRequestService {
    private final RestTemplate restTemplate;
    private final InsuranceHistoryRepositorySupport insuranceHistoryRepositorySupport;

    public InsuranceRequestService(RestTemplateBuilder restTemplateBuilder,InsuranceHistoryRepositorySupport insuranceHistoryRepositorySupport) {
        this.restTemplate = restTemplateBuilder.build();
        this.insuranceHistoryRepositorySupport = insuranceHistoryRepositorySupport;
    }

    private static String serverUrl;
    @Value("${retrofitUrl}")
    public void setServerUrl(String serverUrl) {
        InsuranceRequestService.serverUrl = serverUrl;
    }

    @Scheduled(cron = "0 10 15 * * *")
    public void underWritingBatch(){
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepToday(2);
        List<UnderWritingReq> underWritingReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(UnderWritingReq::new)
                .collect(Collectors.toList());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/rider/underwriting");
        restTemplate.postForObject(builder.toUriString(),underWritingReqList,String.class);
    }
    @Scheduled(cron = "00 50 19 * * *")
    public void endorsementBatch(){
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepYesterday(5);
        List<EndorsementReq> endorsementReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(EndorsementReq::new)
                .collect(Collectors.toList());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/rider/endorsement");
        restTemplate.postForObject(builder.toUriString(),endorsementReqList,String.class);
    }

    @Scheduled(cron = "00 30 20 * * *") //초 분 시 일 월 요일
    public void withDrawRiderBatch(){
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepYesterday(7);
        List<RiderWithdrawReq> withdrawReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(RiderWithdrawReq::new)
                .collect(Collectors.toList());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl +"/rider/withdraw");
        restTemplate.postForObject(builder.toUriString(),withdrawReqList,String.class);
    }

}
