package onboard.persistence.repository;

import onboard.persistence.domain.OnboardingTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingTransactionRepo extends JpaRepository<OnboardingTransactionEntity, String> {
    OnboardingTransactionEntity findFirstByPhoneNumberOrderByCreatedDateDesc(String phoneNumber);
    OnboardingTransactionEntity findFirstByIdOrderByCreatedDateDesc(String id);
}