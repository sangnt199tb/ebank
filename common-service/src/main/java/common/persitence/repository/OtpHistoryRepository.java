package common.persitence.repository;

import common.persitence.domain.OtpHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpHistoryRepository extends JpaRepository<OtpHistory, Long> {
    OtpHistory findFirstByOtpTransactionId(String transId);
}
