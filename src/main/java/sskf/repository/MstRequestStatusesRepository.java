package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstRequestStatusesEntity;

@Repository
public interface MstRequestStatusesRepository extends JpaRepository<MstRequestStatusesEntity, Long> {
    MstRequestStatusesEntity findByName(String name);
}
