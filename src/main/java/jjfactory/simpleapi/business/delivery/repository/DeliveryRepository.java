package jjfactory.simpleapi.business.delivery.repository;

import jjfactory.simpleapi.business.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
