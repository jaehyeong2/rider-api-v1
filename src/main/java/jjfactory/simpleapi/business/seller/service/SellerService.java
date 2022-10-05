package jjfactory.simpleapi.business.seller.service;


import jjfactory.simpleapi.business.accident.repository.AccidentRepositorySupport;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.business.rider.repository.RiderRepositorySupport;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.dto.req.SellerCreate;
import jjfactory.simpleapi.business.seller.dto.req.SellerModify;
import jjfactory.simpleapi.business.seller.dto.res.SellerAccidentRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerDeliveryRes;
import jjfactory.simpleapi.business.seller.dto.res.SellerRiderRes;
import jjfactory.simpleapi.business.seller.repository.SellerRepository;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional
@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final AccidentRepositorySupport accidentRepositorySupport;
    private final DeliveryRepositorySupport deliveryRepositorySupport;
    private final RiderRepositorySupport riderRepositorySupport;
    public Long createSeller(SellerCreate dto){
        String decimalString = dto.getBizNum() + LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));

        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < decimalString.length(); i++) {
            hex.append(Integer.toHexString(Integer.parseInt(decimalString.substring(i, i + 2))));
            i++;
        }

        Seller seller = Seller.create(dto,hex.toString());
        sellerRepository.save(seller);
        return seller.getId();
    }

    public Long modifySellerInfo(String sellerCode,SellerModify dto){
        Seller findSeller = sellerRepository.findByName(dto.getName());
        sellerValidateCheck(sellerCode, findSeller);

        findSeller.modify(dto);
        return findSeller.getId();
    }
    @Transactional(readOnly = true)
    public PagingRes<SellerAccidentRes> findSellerAccident(Pageable pageable,String sellerCode, String sellerName){
        Seller findSeller = sellerRepository.findByName(sellerName);
        sellerValidateCheck(sellerCode,findSeller);

        return new PagingRes<>(accidentRepositorySupport.findAccidentsBySellerName(pageable,sellerName));
    }

    @Transactional(readOnly = true)
    public Integer findTotalCompensation(String sellerCode,String sellerName){
        Seller findSeller = sellerRepository.findByName(sellerName);
        sellerValidateCheck(sellerCode,findSeller);

        return accidentRepositorySupport.findTotalCompensation(sellerName);
    }

    @Transactional(readOnly = true)
    public PagingRes<SellerDeliveryRes> findSellerDeliveries(Pageable pageable, String startDate, String endDate, String sellerCode,String sellerName){
        Seller findSeller = sellerRepository.findByName(sellerName);
        sellerValidateCheck(sellerCode,findSeller);

        return new PagingRes<>(deliveryRepositorySupport.findSellerDeliveries(pageable,startDate,endDate,sellerName));
    }

    @Transactional(readOnly = true)
    public PagingRes<SellerRiderRes> findRidersInSeller(Pageable pageable, String sellerCode, String sellerName){
        Seller findSeller = sellerRepository.findByName(sellerName);
        sellerValidateCheck(sellerCode,findSeller);

        return new PagingRes(riderRepositorySupport.findRiderInfoInSeller(pageable,findSeller.getId()));
    }

    private void sellerValidateCheck(String sellerCode, Seller seller) {
        if(!seller.getSellerCode().equals(sellerCode)){
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

}
