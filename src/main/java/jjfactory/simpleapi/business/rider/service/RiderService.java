package jjfactory.simpleapi.business.rider.service;


import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
@Service
public class RiderService {
    private final RiderRepository riderRepository;
}
