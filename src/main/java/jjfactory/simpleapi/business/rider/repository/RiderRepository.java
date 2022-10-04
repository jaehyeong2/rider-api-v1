package jjfactory.simpleapi.business.rider.repository;

import jjfactory.simpleapi.business.rider.domain.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider,Long> {
    Optional<Rider> findByPhone(String phone);
    Optional<Rider> findByDriverId(String driverId);
    Optional<Rider> findByLoginId(String loginId);

}
