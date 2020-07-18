package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstStoreEntity;

@Repository
public interface MstStoreRepository extends JpaRepository<MstStoreEntity, String>, SearchRSQLRepository<MstStoreEntity> {
}
