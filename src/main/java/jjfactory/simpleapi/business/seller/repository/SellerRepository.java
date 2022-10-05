package jjfactory.simpleapi.business.seller.repository;

import jjfactory.simpleapi.business.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findBySellerCode(String sellerCode);
    Seller findByName(String name);
}
