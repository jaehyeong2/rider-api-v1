package jjfactory.simpleapi.business.accident.service;


import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.accident.repository.AccidentRepository;
import jjfactory.simpleapi.business.accident.repository.AccidentRepositorySupport;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.global.dto.res.PagingRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Service
public class AccidentService {
    private final AccidentRepository accidentRepository;
    private final AccidentRepositorySupport accidentRepositorySupport;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRepositorySupport deliveryRepositorySupport;

    public void createAccident(AccidentCreate dto){
        Delivery delivery = deliveryRepository.findByDeliveryId(dto.getDeliveryId()).orElseThrow(NoSuchElementException::new);
        Accident accident = Accident.create(dto, delivery);
        accidentRepository.save(accident);

    }

    public PagingRes<Accident> findAccidentsByPhone(Pageable pageable, String phone){
        return new PagingRes(accidentRepositorySupport.findAccidentsByPhone(pageable,phone));
    }

    public Integer findTotalCompensation(String sellerCode){
        return accidentRepositorySupport.findTotalCompensation(sellerCode);
    }

}
