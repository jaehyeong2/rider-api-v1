package jjfactory.simpleapi.domain.accident.repository;

import jjfactory.simpleapi.domain.accident.domain.Accident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident,Long> {
}
