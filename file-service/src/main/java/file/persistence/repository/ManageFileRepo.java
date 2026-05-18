package file.persistence.repository;

import file.persistence.domain.ManageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageFileRepo extends JpaRepository<ManageFileEntity, String> {
    ManageFileEntity findFirstById(String id);
}
