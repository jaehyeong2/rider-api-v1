package jjfactory.simpleapi.domain.delivery.repository;

import jjfactory.simpleapi.domain.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
