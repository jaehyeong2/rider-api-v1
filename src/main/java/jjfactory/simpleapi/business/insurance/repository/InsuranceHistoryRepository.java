package jjfactory.simpleapi.business.insurance.repository;

import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceHistoryRepository extends JpaRepository<InsuranceHistory,Long> {
}
