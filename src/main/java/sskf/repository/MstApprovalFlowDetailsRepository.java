package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstApprovalFlowDetailsEntity;

@Repository
public interface MstApprovalFlowDetailsRepository extends JpaRepository<MstApprovalFlowDetailsEntity, String>, SearchRSQLRepository<MstApprovalFlowDetailsEntity> {
}
