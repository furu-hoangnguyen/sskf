package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sskf.model.entity.OperationHistoriesEntity;

import java.util.List;

@Repository
public interface OperationHistoriesRepository extends JpaRepository<OperationHistoriesEntity, Long> , SearchRSQLRepository<OperationHistoriesEntity> {

    List<OperationHistoriesEntity> findByRequestsEntityCdAndMstRequestCommentActionsEntityNameAndParentOperationHistoriesEntityIsNull(Long requestCd, String commentActionName);

    @Query(value = "SELECT o.shain_cd FROM operation_histories o WHERE o.request_cd=:requestCd ORDER BY o.created_at ASC " +
            "LIMIT 1", nativeQuery = true)
    String findCreatedBy(@Param("requestCd") Long requestCd);

    @Query(value = "SELECT o.step_number from operation_histories o " +
            "inner join mst_request_comment_actions cmta on o.mst_request_comment_action_cd = cmta.cd and cmta.name = '差し戻し' " +
            "where request_cd = :requestCd ORDER BY o.created_at ASC " +
            "LIMIT 1", nativeQuery = true)
    Byte getStepNumberMaxWhenSendBack(@Param("requestCd") Long requestCd);
}
