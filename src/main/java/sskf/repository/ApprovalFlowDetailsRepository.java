package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;

import java.util.List;

@Repository
public interface ApprovalFlowDetailsRepository extends JpaRepository<ApprovalFlowDetailsEntity, Long>, SearchRSQLRepository<ApprovalFlowDetailsEntity> {

    List<ApprovalFlowDetailsEntity> findByRequestsEntityAndStepNumberAndShainEntity_ShainCd(RequestsEntity requestsEntity, Byte stepNumber, String ShainCd);

    List<ApprovalFlowDetailsEntity> findByRequestsEntityAndStepNumberInAndShainEntity_ShainCd(RequestsEntity requestsEntity, List<Byte> stepNumbers, String ShainCd);

    @Query(value = "SELECT ap.shain_cd FROM approvalflow_details ap " +
            "WHERE ap.request_cd = :requestsCd AND ap.step_number = :stepNumber", nativeQuery = true)
    List<String> getShainCdByStepNumberAndRequestsCd(@Param("stepNumber") Byte stepNumber, @Param("requestsCd") Long requestsCd);

    ApprovalFlowDetailsEntity findByRequestsEntityAndStepNumberAndAndShainEntityAndIsDeputy(RequestsEntity requestsEntity, Byte stepNumber, ShainEntity ShainCd, Boolean isDeputy);
}
