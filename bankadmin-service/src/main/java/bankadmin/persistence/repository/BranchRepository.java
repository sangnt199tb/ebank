package bankadmin.persistence.repository;

import bankadmin.persistence.domain.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<BankEntity, String> {
}
