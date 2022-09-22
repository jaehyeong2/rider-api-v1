package jjfactory.simpleapi.business.delivery.repository;

import jjfactory.simpleapi.business.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findByDeliveryId(String deliveryId);
}
