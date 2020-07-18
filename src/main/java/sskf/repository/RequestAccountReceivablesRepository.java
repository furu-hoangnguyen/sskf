package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestAccountsReceivablesEntity;

@Repository
public interface RequestAccountReceivablesRepository extends JpaRepository<RequestAccountsReceivablesEntity, Long>, SearchRSQLRepository<RequestAccountsReceivablesEntity> {
}
