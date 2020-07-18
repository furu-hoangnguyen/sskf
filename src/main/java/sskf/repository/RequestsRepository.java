package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestsEntity;

@Repository
public interface RequestsRepository extends JpaRepository<RequestsEntity, Long>, SearchRSQLRepository<RequestsEntity>, JpaSpecificationExecutor<RequestsEntity> {

    RequestsEntity findByCdAndIsDeleted(Long cd, Byte isDeleted);

    @Query(value = "select count(distinct r.cd)\n" +
            "from requests r\n" +
            "inner join mst_request_statuses mrs\n" +
            "on r.status_cd = mrs.cd\n" +
            "inner join operation_histories oh\n" +
            "on r.cd = oh.request_cd\n" +
            "inner join mst_request_comment_actions mrca\n" +
            "on oh.mst_request_comment_action_cd = mrca.cd\n" +
            "where mrs.name = '作成中' and r.is_deleted = 0\n" +
            "and mrca.name = '作成途中保存'\n" +
            "and oh.shain_cd = ?1", nativeQuery = true)
    int countRequestCreate(String shainCd);

    @Query(value = "select count(distinct r.cd)\n" +
            "from requests r\n" +
            "inner join mst_request_statuses mrs\n" +
            "on r.status_cd = mrs.cd\n" +
            "inner join request_accounts_receivables rar\n" +
            "on r.cd = rar.request_cd\n" +
            "inner join request_accounts_receivable_details rard\n" +
            "on rar.cd = rard.request_accounts_receivable_cd\n" +
            "where mrs.name = '確認待ち' and r.is_deleted = 0\n" +
            "and rard.shain_cd = ?1 and rard.is_checked=0", nativeQuery = true)
    int countRequestWaitingConfirm(String shainCd);

    @Query(value = "select count(distinct r.cd)\n" +
            "from requests r\n" +
            "inner join mst_request_statuses mrs\n" +
            "on r.status_cd = mrs.cd\n" +
            "inner join approvalflow_details ad\n" +
            "on r.cd = ad.request_cd\n" +
            "where mrs.name = '申請待ち'\n" +
            "and r.is_deleted = 0\n" +
            "and ad.step_number = r.step_number\n" +
            "and ad.shain_cd = ?1", nativeQuery = true)
    int countRequestWaitingApply(String shainCd);

    @Query(value = "select count(distinct r.cd)\n" +
            "from requests r\n" +
            "inner join mst_request_statuses mrs\n" +
            "on r.status_cd = mrs.cd\n" +
            "inner join approvalflow_details ad\n" +
            "on r.cd = ad.request_cd\n" +
            "where mrs.name = '承認待ち'\n" +
            "and r.is_deleted = 0\n" +
            "and ad.step_number = r.step_number\n" +
            "and ad.shain_cd = ?1", nativeQuery = true)
    int countRequestWaitingApprove(String shainCd);

    @Query(value = "select count(distinct r.cd)\n" +
            "from requests r\n" +
            "inner join mst_request_statuses mrs\n" +
            "on r.status_cd = mrs.cd\n" +
            "where mrs.name = '決済確認待ち' and r.is_deleted=0;",
            nativeQuery = true)
    int countRequestWaitingConfirmSettlement(String shainCd);

}
