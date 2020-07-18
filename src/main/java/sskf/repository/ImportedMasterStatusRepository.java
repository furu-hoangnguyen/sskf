package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sskf.model.entity.ImportedMasterStatusEntity;

@Repository
public interface ImportedMasterStatusRepository extends JpaRepository<ImportedMasterStatusEntity, Long>, SearchRSQLRepository<ImportedMasterStatusEntity>, JpaSpecificationExecutor<ImportedMasterStatusEntity> {

}
