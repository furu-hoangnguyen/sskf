package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstYakushokuEntity;

import java.util.List;

@Repository
public interface MstYakushokuRepository extends JpaRepository<MstYakushokuEntity, String> {
    List<MstYakushokuEntity> findAllByIsDeletedIs(Byte isDeleted);
}
