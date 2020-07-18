package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstBumonEntity;

import java.util.List;

@Repository
public interface BumonRepository extends JpaRepository<MstBumonEntity, String> {
    List<MstBumonEntity> findByBumonCdNkIsNotNull();

    List<MstBumonEntity> findByBumonCdIn(List<String> bumonCds);
}
