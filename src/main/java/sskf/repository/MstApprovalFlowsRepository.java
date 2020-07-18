package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstApprovalFlowsEntity;

@Repository
public interface MstApprovalFlowsRepository extends JpaRepository<MstApprovalFlowsEntity, String>, SearchRSQLRepository<MstApprovalFlowsEntity> {
    MstApprovalFlowsEntity findFirstByCd(String cd);
}
