package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;

@Repository
public interface DetailsForAccountsReceivablesRepository extends JpaRepository<DetailsForAccountsReceivablesEntity, Long> {
}
