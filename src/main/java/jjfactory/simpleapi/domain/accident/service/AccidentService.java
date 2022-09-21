package jjfactory.simpleapi.domain.accident.service;


import jjfactory.miniapi.business.accident.repository.AccidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AccidentService {
    private final AccidentRepository accidentRepository;
}
