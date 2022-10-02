package jjfactory.simpleapi.business.delivery.repository;

import jjfactory.simpleapi.business.delivery.domain.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory,Long> {
}
