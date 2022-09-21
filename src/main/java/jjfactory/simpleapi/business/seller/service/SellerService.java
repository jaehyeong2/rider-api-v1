package jjfactory.simpleapi.business.seller.service;


import jjfactory.simpleapi.business.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SellerService {
    private final SellerRepository sellerRepository;
}
