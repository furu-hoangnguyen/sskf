package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstTantoEntity;

@Repository
public interface MstTantoRepository extends JpaRepository<MstTantoEntity, String>, SearchRSQLRepository<MstTantoEntity> {
}
