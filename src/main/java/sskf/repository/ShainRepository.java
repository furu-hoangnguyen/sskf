package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;

import java.util.List;

@Repository
public interface ShainRepository extends JpaRepository<ShainEntity, String>, SearchRSQLRepository<ShainEntity> {
    ShainEntity findByShainCd(String shainCd);

    List<ShainEntity> findByShainCdNkIsNotNullAndMstBumonEntity_BumonCd(String bumonCd);

    @Query(value = "select DISTINCT(shain.shain_cd)\n" +
            "from  shain shain \n" +
            "inner join mst_rel_yakushoku_shain mst_rel_yakushoku_shain on shain.shain_cd = mst_rel_yakushoku_shain.shain_cd\n" +
            "inner join mst_yakushoku mst_yakushoku on mst_rel_yakushoku_shain.yakusyoku_cd = mst_yakushoku.cd  \n" +
            "inner join mst_approvalflow_details mst_approvalflow_details on mst_approvalflow_details.yakusyoku_cd = mst_yakushoku.cd and shain.bumon_cd = mst_approvalflow_details.bumon_cd\n" +
            "where mst_approvalflow_details.approvalflow_cd = :mstApprovalFlowCd and mst_approvalflow_details.step_number = :stepNumber and mst_approvalflow_details.is_deputy = 0", nativeQuery = true)
    List<String> listShainCdApproveByMstApprovalFlowsCdAndStepNumber(@Param("mstApprovalFlowCd") String mstApprovalFlowCd, @Param("stepNumber") Integer stepNumber);

}
