package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstBumonEntity;

@Repository
public interface MstBumonRepository extends JpaRepository<MstBumonEntity, String>, SearchRSQLRepository<MstBumonEntity> {
}
