package jjfactory.simpleapi.business.insurance.service;


import jjfactory.simpleapi.business.insurance.domain.HistoryType;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import jjfactory.simpleapi.business.insurance.domain.Reject;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementCancelRes;
import jjfactory.simpleapi.business.insurance.dto.res.EndorsementRes;
import jjfactory.simpleapi.business.insurance.dto.res.RiderCountRes;
import jjfactory.simpleapi.business.insurance.dto.res.UnderWritingRes;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.insurance.repository.RejectRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.repository.RiderRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class InsuranceResultService {
    private final InsuranceHistoryRepository insuranceHistoryRepository;
    private final RiderRepositorySupport riderRepositorySupport;
    private final RejectRepository rejectRepository;

    public RiderCountRes underWritingResult(List<UnderWritingRes> results){
        List<InsuranceHistory> histories = new ArrayList<>();
        List<Reject> rejects = new ArrayList<>();

            results.forEach(result->{
                Rider rider = riderRepositorySupport.findRiderByDriverId(result.getDriverId());
                // 승인이 아닌경우
                    if (!result.getResult().equals("accepted")) {
                        // 리뷰중이거나, 조건부 승인인경우
                        if(result.getResult().equals("in_review") || result.getResult().equals("accepted_noinsure")){
                            InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.NEED_RETRY,3);
                            histories.add(insuranceHistory);
                        }else{
                            // 승인이 거절당한경우
                            InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.REJECTED,3);
                            histories.add(insuranceHistory);
                            Reject reject = Reject.create(insuranceHistory, result.getResult());
                            rejects.add(reject);
                        }
                    }
                    //승인된 경우
                    else {
                        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.ACCEPTED,3);
                        histories.add(insuranceHistory);
                    }

            });

        insuranceHistoryRepository.saveAll(histories);
        rejectRepository.saveAll(rejects);

        return new RiderCountRes(results.size());
    }

    public RiderCountRes endorsementResult(List<EndorsementRes> results){
        List<InsuranceHistory> histories = new ArrayList<>();
        List<Reject> rejects = new ArrayList<>();

            results.forEach(result->{
                Rider rider = riderRepositorySupport.findRiderByDriverId(result.getDriverId());

                    // 기명 요청이 승인되지 않았을경우
                    if (!result.getResult().equals("accepted")) {
                        //거절된 내역과 거절 사유 각 리스트에 저장
                        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.REJECTED,6);
                        histories.add(insuranceHistory);
                        Reject reject = Reject.create(insuranceHistory, result.getResult());
                        rejects.add(reject);
                    }
                    // 승인된 경우
                    else {
                        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.ACCEPTED,6);
                        histories.add(insuranceHistory);

                        rider.updateInsuranceApply(true);
                    }
            });

        insuranceHistoryRepository.saveAll(histories);
        rejectRepository.saveAll(rejects);

        return new RiderCountRes(results.size());
    }

    public RiderCountRes cancelResult(List<EndorsementCancelRes> results){
        List<InsuranceHistory> histories = new ArrayList<>();
        List<Reject> rejects = new ArrayList<>();

            results.forEach(result->{
                Rider rider = riderRepositorySupport.findRiderByDriverId(result.getDriverId());

                    if (!result.getResult().equals("accepted")) {
                        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.REJECTED,8);
                        Reject reject = Reject.create(insuranceHistory, result.getResult());
                        histories.add(insuranceHistory);
                        rejects.add(reject);

                    } else {
                        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.ACCEPTED,8);
                        histories.add(insuranceHistory);

                        rider.updateInsuranceApply(false);
                    }
            });

        insuranceHistoryRepository.saveAll(histories);
        rejectRepository.saveAll(rejects);

        return new RiderCountRes(results.size());
    }

}
