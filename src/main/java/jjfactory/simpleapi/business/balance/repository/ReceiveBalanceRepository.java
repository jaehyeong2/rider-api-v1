package jjfactory.simpleapi.business.balance.repository;

import jjfactory.simpleapi.business.balance.domain.ReceiveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiveBalanceRepository extends JpaRepository<ReceiveBalance,Long> {
}
