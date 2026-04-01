package customer.persistence.repository;

import customer.persistence.domain.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    CustomerEntity findByCustomerId(String id);
}
