package jjfactory.simpleapi.business.accident.repository;

import jjfactory.simpleapi.business.accident.domain.Accident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident,Long> {
}
