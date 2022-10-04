package jjfactory.simpleapi.business.rider.service;


import jjfactory.simpleapi.business.insurance.domain.HistoryType;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.dto.res.RiderInsuranceStatusRes;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import jjfactory.simpleapi.global.uitls.enc.AES_Encryption;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
@Service
public class RiderService {
    private final RiderRepository riderRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;

    @Transactional(readOnly = true)
    public RiderInsuranceStatusRes getRiderInsuranceStatus(Rider rider){
        return new RiderInsuranceStatusRes(rider);
    }

    public String withdraw(Rider rider){
        insuranceValidationCheck(rider);

        InsuranceHistory history = InsuranceHistory.create(rider, HistoryType.REQUEST,7);
        insuranceHistoryRepository.save(history);

        return "Y";
    }


    private void insuranceValidationCheck(Rider rider) {
        boolean insuranceStatus = rider.isInsuranceApply();
        if(!insuranceStatus){
            throw new BusinessException(ErrorCode.NOT_ENDORSED_USER);
        }
    }
}
