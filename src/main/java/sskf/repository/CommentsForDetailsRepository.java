package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sskf.model.entity.CommentsForDetailsEntity;

import java.util.List;

@Repository
public interface CommentsForDetailsRepository extends JpaRepository<CommentsForDetailsEntity, Long>, SearchRSQLRepository<CommentsForDetailsEntity> {

    @Query(value = "SELECT cfd.comment, Max(cfd.created_at) " +
            "FROM comments_for_details cfd " +
            "JOIN request_accounts_receivable_details rard on cfd.request_accounts_receivable_detail_cd = rard.cd " +
            "WHERE rard.store_g_cd =:storeGCd AND cfd.is_deleted = 0 " +
            "GROUP BY (cfd.comment) " +
            "ORDER BY Max(cfd.created_at) DESC", nativeQuery = true)
    List<Object> listOldComment(@Param("storeGCd") String storeGCd);

    List<CommentsForDetailsEntity> findByRequestAccountsReceivableDetailsEntityCdAndIsDeletedOrderByCdAsc(Long requestAccountCd, Byte isDeleted);

    @Modifying
    @Query(value = "update comments_for_details c set c.is_capable_of_being_deleted = 0, c.operation_history_cd = :operationHistoriesEntitieCd " +
            "where EXISTS ( " +
            "select rard.cd from request_accounts_receivable_details rard " +
            "inner join request_accounts_receivables rar on rard.request_accounts_receivable_cd = rar.cd " +
            "where rar.cd = :requestAccountsReceivablesCd " +
            "and c.request_accounts_receivable_detail_cd = rard.cd ) and c.is_capable_of_being_deleted = 1 and c.is_deleted = 0 and c.operation_history_cd IS NULL", nativeQuery = true)
    void updateIsCapableOfBeingDeleted(@Param("requestAccountsReceivablesCd") Long requestAccountsReceivablesCd,@Param("operationHistoriesEntitieCd") Long operationHistoriesEntitieCd);
}
