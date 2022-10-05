package jjfactory.simpleapi.business.accident.service;


import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.accident.dto.req.AccidentCreate;
import jjfactory.simpleapi.business.accident.repository.AccidentRepository;
import jjfactory.simpleapi.business.accident.repository.AccidentRepositorySupport;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepository;
import jjfactory.simpleapi.business.delivery.repository.DeliveryRepositorySupport;
import jjfactory.simpleapi.business.rider.domain.Rider;
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

    public Long createAccident(AccidentCreate dto){
        Delivery delivery = deliveryRepository.findByDeliveryId(dto.getDeliveryId()).orElseThrow(NoSuchElementException::new);
        Accident accident = Accident.create(dto, delivery);
        accidentRepository.save(accident);

        return accident.getId();
    }

    @Transactional(readOnly = true)
    public PagingRes<Accident> findMyAccidentsByPhone(Pageable pageable, Rider rider){
        return new PagingRes(accidentRepositorySupport.findAccidentsByPhone(pageable,rider.getPhone()));
    }
}
