package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;

@Repository
public interface RequestAccountsReceivableDetailsRepository extends JpaRepository<RequestAccountsReceivableDetailsEntity, Long>, SearchRSQLRepository<RequestAccountsReceivableDetailsEntity> {

}
