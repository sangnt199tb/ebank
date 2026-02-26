package onboard.persistence.repository;

import onboard.persistence.domain.SdkConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SdkConfigRepo extends JpaRepository<SdkConfigEntity, String> {
    Optional<SdkConfigEntity> findById(String id);
}
