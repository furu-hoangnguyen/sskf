package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstRequestTypesEntity;

@Repository
public interface MstRequestTypeRepository extends JpaRepository<MstRequestTypesEntity, Long>, SearchRSQLRepository<MstRequestTypesEntity> {
    MstRequestTypesEntity findByName(String name);
}
