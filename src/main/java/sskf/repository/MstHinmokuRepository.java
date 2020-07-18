package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstHinmokuEntity;

@Repository
public interface MstHinmokuRepository extends JpaRepository<MstHinmokuEntity, String>, SearchRSQLRepository<MstHinmokuEntity> {
}
