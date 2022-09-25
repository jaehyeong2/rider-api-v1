package jjfactory.simpleapi.business.insurance.service;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class InsuranceRequestService {
    private final RiderRepository riderRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;
    private final InsuranceHistoryRepositorySupport insuranceHistoryRepositorySupport;

    @Async
    @Scheduled(cron = "0 20 04 * * *") //초 분 시 일 월 요일
    public void underWritingBatch() throws Exception {
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepToday(2);
        List<UnderWritingReq> underWritingReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(UnderWritingReq::new)
                .collect(Collectors.toList());

        RetrofitConfig<UnderWritingReq> retrofitConfig = new RetrofitConfig<>();

        //레트로핏에서 execute까지 있으면 함수실행, 바디 까지 찍으면 값 응답 값 추출가능
        retrofitConfig.create(RetrofitApi.class).underWritingRetrofit(underWritingReqList).execute().body();

    }

    @Async
    @Scheduled(cron = "00 50 19 * * *") //초 분 시 일 월 요일
    public void endorsementBatch() throws Exception {
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepYesterday(5);
        List<EndorsementReq> endorsementReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(EndorsementReq::new)
                .collect(Collectors.toList());

        RetrofitConfig<EndorsementReq> retrofitConfig = new RetrofitConfig<>();
        retrofitConfig.create(RetrofitApi.class).endorsementRetrofit(endorsementReqList).execute().body();
    }

    @Async
    @Scheduled(cron = "00 50 19 * * *") //초 분 시 일 월 요일
    public void withDrawRiderBatch() throws Exception {
        List<InsuranceHistory> histories = insuranceHistoryRepositorySupport.findRequestsByInsuranceStepYesterday(7);
        List<RiderWithdrawReq> withdrawReqList = histories.stream()
                .map(InsuranceHistory::getRider)
                .map(RiderWithdrawReq::new)
                .collect(Collectors.toList());

        RetrofitConfig<RiderWithdrawReq> retrofitConfig = new RetrofitConfig<>();
        retrofitConfig.create(RetrofitApi.class).withdrawRetrofit(withdrawReqList).execute().body();
    }

}
