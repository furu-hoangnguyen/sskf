package sskf.specs;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.RequestEntity;
import org.springframework.util.ObjectUtils;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestNumbersEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.FilterRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

public class RequestSpecification {

    public static final String SEND_BACK = "差し戻し";

    /**
     * mst_request_statuses.cd = 1 -> MST_REQUEST_STATUSES_NAME.get(0)
     * mst_request_statuses.cd = 2 -> MST_REQUEST_STATUSES_NAME.get(1)
     * mst_request_statuses.cd = 3 -> MST_REQUEST_STATUSES_NAME.get(2)
     * mst_request_statuses.cd = 4 -> MST_REQUEST_STATUSES_NAME.get(3)
     * mst_request_statuses.cd = 5 -> MST_REQUEST_STATUSES_NAME.get(4)
     * mst_request_statuses.cd = 6 -> MST_REQUEST_STATUSES_NAME.get(5)
     */
    public static final List<String> MST_REQUEST_STATUSES_NAME = Arrays.asList("作成中", "確認待ち", "申請待ち", "承認待ち", "却下", "決済確認待ち");

    /**
     * mst_request_comment_actions.cd = 1 -> MST_REQUEST_COMMENT_ACTIONS_NAME.get(0)
     * mst_request_comment_actions.cd = 2 -> MST_REQUEST_COMMENT_ACTIONS_NAME.get(1)
     */
    public static final List<String> MST_REQUEST_COMMENT_ACTIONS_NAME = Arrays.asList("作成途中保存", "作成");

    // requests.is_deleted=0
    public static Specification<RequestsEntity> hasNotDeleted() {

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            return criteriaBuilder.equal(root.get("isDeleted"), 0);
        };
    }

    public static Specification<RequestsEntity> filterRequestInProgress(String shainCd, FilterRequest filterRequest) {

        return (root, query, criteriaBuilder) -> {
            // requests.status_cd in (1,2,3,4)
            Join<RequestsEntity, MstRequestStatusesEntity> mstRequestStatusesEntity = root.join("mstRequestStatusesEntity", JoinType.INNER);
            CriteriaBuilder.In<String> inClauseStatusCd = criteriaBuilder.in(mstRequestStatusesEntity.get("name"));
            inClauseStatusCd.value(MST_REQUEST_STATUSES_NAME.get(0));
            inClauseStatusCd.value(MST_REQUEST_STATUSES_NAME.get(1));
            inClauseStatusCd.value(MST_REQUEST_STATUSES_NAME.get(2));
            inClauseStatusCd.value(MST_REQUEST_STATUSES_NAME.get(3));

            // approvalflow_details1.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesDefault = root.join("approvalFlowDetailsEntities", JoinType.INNER);
            Join<ApprovalFlowDetailsEntity, ShainEntity> shainEntityDefault = approvalFlowDetailsEntitiesDefault.join("shainEntity", JoinType.INNER);

            Predicate predicateShainCdDefault = criteriaBuilder.equal(shainEntityDefault.get("shainCd"), shainCd);

            // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.LEFT);
            Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT);
            Join<RequestAccountsReceivableDetailsEntity, ShainEntity> shainEntityReceivableDetail = requestAccountsReceivableDetailsEntities.join("shainEntity", JoinType.LEFT);

            Predicate predicateShainCdReceivableDetail = criteriaBuilder.equal(shainEntityReceivableDetail.get("shainCd"), shainCd);

            // operation_histories.mst_request_comment_action_cd in (1,2) and
            // operation_histories.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, OperationHistoriesEntity> operationHistoriesEntities = root.join("operationHistoriesEntities", JoinType.INNER);

            Join<OperationHistoriesEntity, MstRequestCommentActionsEntity> mstRequestCommentActionsEntity = operationHistoriesEntities.join("mstRequestCommentActionsEntity", JoinType.INNER);

            CriteriaBuilder.In<String> inClauseRequestCommentAction = criteriaBuilder.in(mstRequestCommentActionsEntity.get("name"));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(0));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(1));

            Join<OperationHistoriesEntity, ShainEntity> shainEntityOperationHistory = operationHistoriesEntities.join("shainEntity", JoinType.INNER);
            Predicate predicateOperationHistory = criteriaBuilder.equal(shainEntityOperationHistory.get("shainCd"), shainCd);

            Predicate predicateInProgressDefault = criteriaBuilder.and(inClauseStatusCd,
                    criteriaBuilder.or(predicateShainCdDefault, predicateShainCdReceivableDetail,
                            criteriaBuilder.and(inClauseRequestCommentAction, predicateOperationHistory)));

            List<Predicate> predicateFilters = new ArrayList<>();

            if (!ObjectUtils.isEmpty(filterRequest)) {
                // 25
                if (!isEmpty(filterRequest.getConfirmPerson())) {

                    Predicate predicateConfirmPerson = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%");
                    predicateFilters.add(predicateConfirmPerson);
                }

                // 26
                if (!isEmpty(filterRequest.getApplicant())) {
                    Predicate predicateApplicantName = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getApplicant() + "%");
                    Predicate predicateApplicantStep = criteriaBuilder.equal(approvalFlowDetailsEntitiesDefault.get("stepNumber"), 1);
                    predicateFilters.add(criteriaBuilder.and(predicateApplicantName, predicateApplicantStep));
                }

                if (!isEmpty(filterRequest.getApprover())) {
                    // 27
                    Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesApprover = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                    CriteriaBuilder.In<Integer> inClauseApprover = criteriaBuilder.in(approvalFlowDetailsEntitiesApprover.get("stepNumber"));
                    inClauseApprover.value(2);
                    inClauseApprover.value(3);
                    inClauseApprover.value(4);
                    inClauseApprover.value(5);
                    approvalFlowDetailsEntitiesApprover.on(inClauseApprover);

                    Predicate predicateApprover = criteriaBuilder.like(approvalFlowDetailsEntitiesApprover.get("shainNm"), "%" + filterRequest.getApprover() + "%");
                    predicateFilters.add(predicateApprover);
                }

                // 28. Status - Search
                if (!isEmpty(filterRequest.getStatus())) {
                    if (filterRequest.getStatus().equals(SEND_BACK)) {
                        Predicate predicateIsSentBack = criteriaBuilder.equal(root.get("isSentBack"), 1);
                        predicateFilters.add(predicateIsSentBack);
                    } else {
                        Predicate predicateName = criteriaBuilder.equal(mstRequestStatusesEntity.get("name"), filterRequest.getStatus());
                        predicateFilters.add(predicateName);
                    }
                }

            }

            predicateFilters.add(predicateInProgressDefault);
            Predicate[] predicateFilterArray = predicateFilters.toArray(new Predicate[predicateFilters.size()]);

            return criteriaBuilder.and(predicateFilterArray);
        };
    }

    public static Specification<RequestsEntity> filterRequestAll(String shainCd, FilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateFilters = new ArrayList<>();

            if (!ObjectUtils.isEmpty(filterRequest)) {
                // 24
                if (!isEmpty(filterRequest.getRelatedPerson())) {
                    Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.LEFT);
                    Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT);
                    Predicate predicateRelatedPersonRequestAccountsReceivableDetail = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getRelatedPerson() + "%");

                    Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesDefault = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                    Predicate predicateRelatedPersonApprovalFlowDetail = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getRelatedPerson() + "%");

                    Predicate predicateRelatedPerson = criteriaBuilder.or(predicateRelatedPersonRequestAccountsReceivableDetail, predicateRelatedPersonApprovalFlowDetail);

                    predicateFilters.add(predicateRelatedPerson);

                    // 25
                    if (!isEmpty(filterRequest.getConfirmPerson())) {
                        Predicate predicateConfirmPerson = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%");
                        predicateFilters.add(predicateConfirmPerson);
                    }

                    // 26
                    if (!isEmpty(filterRequest.getApplicant())) {
                        Predicate predicateApplicantName = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getApplicant() + "%");
                        Predicate predicateApplicantStep = criteriaBuilder.equal(approvalFlowDetailsEntitiesDefault.get("stepNumber"), 1);
                        predicateFilters.add(criteriaBuilder.and(predicateApplicantName, predicateApplicantStep));
                    }

                    // 27
                    if (!isEmpty(filterRequest.getApprover())) {

                        // 27
                        Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesApprover = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                        CriteriaBuilder.In<Integer> inClauseApprover = criteriaBuilder.in(approvalFlowDetailsEntitiesApprover.get("stepNumber"));
                        inClauseApprover.value(2);
                        inClauseApprover.value(3);
                        inClauseApprover.value(4);
                        inClauseApprover.value(5);
                        approvalFlowDetailsEntitiesApprover.on(inClauseApprover);

                        Predicate predicateApprover = criteriaBuilder.like(approvalFlowDetailsEntitiesApprover.get("shainNm"), "%" + filterRequest.getApprover() + "%");
                        predicateFilters.add(predicateApprover);
                    }

                } else {

                    // 25
                    if (!isEmpty(filterRequest.getConfirmPerson())) {
                        Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.LEFT);
                        Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT);
                        Predicate predicateConfirmPerson = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%");
                        predicateFilters.add(predicateConfirmPerson);
                    }

                    // 26
                    if (!isEmpty(filterRequest.getApplicant())) {
                        Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesDefault = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                        Predicate predicateApplicantName = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getApplicant() + "%");
                        Predicate predicateApplicantStep = criteriaBuilder.equal(approvalFlowDetailsEntitiesDefault.get("stepNumber"), 1);
                        predicateFilters.add(criteriaBuilder.and(predicateApplicantName, predicateApplicantStep));
                    }

                    if (!isEmpty(filterRequest.getApprover())) {
                        // 27
                        Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesApprover = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                        CriteriaBuilder.In<Integer> inClauseApprover = criteriaBuilder.in(approvalFlowDetailsEntitiesApprover.get("stepNumber"));
                        inClauseApprover.value(2);
                        inClauseApprover.value(3);
                        inClauseApprover.value(4);
                        inClauseApprover.value(5);
                        approvalFlowDetailsEntitiesApprover.on(inClauseApprover);

                        Predicate predicateApprover = criteriaBuilder.like(approvalFlowDetailsEntitiesApprover.get("shainNm"), "%" + filterRequest.getApprover() + "%");
                        predicateFilters.add(predicateApprover);
                    }
                }

                // 28. Status - Search
                if (!isEmpty(filterRequest.getStatus())) {
                    if (filterRequest.getStatus().equals(SEND_BACK)) {
                        Predicate predicateIsSentBack = criteriaBuilder.equal(root.get("isSentBack"), 1);
                        predicateFilters.add(predicateIsSentBack);
                    } else {
                        Join<RequestsEntity, MstRequestStatusesEntity> mstRequestStatusesEntity = root.join("mstRequestStatusesEntity", JoinType.INNER);
                        Predicate predicateName = criteriaBuilder.equal(mstRequestStatusesEntity.get("name"), filterRequest.getStatus());
                        predicateFilters.add(predicateName);
                    }
                }

            }

            Predicate[] predicateFilterArray = predicateFilters.toArray(new Predicate[predicateFilters.size()]);

            return criteriaBuilder.and(predicateFilterArray);
        };
    }

    public static Specification<RequestsEntity> filterRequestSendBack(String shainCd, FilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesDefault = root.join("approvalFlowDetailsEntities", JoinType.INNER);
            Join<ApprovalFlowDetailsEntity, ShainEntity> shainEntityDefault = approvalFlowDetailsEntitiesDefault.join("shainEntity", JoinType.INNER);

            Predicate predicateShainCdDefault = criteriaBuilder.equal(shainEntityDefault.get("shainCd"), shainCd);

            // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.LEFT);
            Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT);
            Join<RequestAccountsReceivableDetailsEntity, ShainEntity> shainEntityReceivableDetail = requestAccountsReceivableDetailsEntities.join("shainEntity", JoinType.LEFT);

            Predicate predicateShainCdReceivableDetail = criteriaBuilder.equal(shainEntityReceivableDetail.get("shainCd"), shainCd);

            // operation_histories.mst_request_comment_action_cd in (1,2) and
            // operation_histories.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, OperationHistoriesEntity> operationHistoriesEntities = root.join("operationHistoriesEntities", JoinType.INNER);

            Join<OperationHistoriesEntity, MstRequestCommentActionsEntity> mstRequestCommentActionsEntity = operationHistoriesEntities.join("mstRequestCommentActionsEntity", JoinType.INNER);

            CriteriaBuilder.In<String> inClauseRequestCommentAction = criteriaBuilder.in(mstRequestCommentActionsEntity.get("name"));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(0));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(1));

            Join<OperationHistoriesEntity, ShainEntity> shainEntityOperationHistory = operationHistoriesEntities.join("shainEntity", JoinType.INNER);
            Predicate predicateOperationHistory = criteriaBuilder.equal(shainEntityOperationHistory.get("shainCd"), shainCd);


            Predicate predicateSendBackDefault =  criteriaBuilder.or(predicateShainCdDefault, predicateShainCdReceivableDetail,
                    criteriaBuilder.and(inClauseRequestCommentAction, predicateOperationHistory));

            List<Predicate> predicateFilters = new ArrayList<>();

            if (!ObjectUtils.isEmpty(filterRequest)) {
                // 25
                if (!isEmpty(filterRequest.getConfirmPerson())) {

                    Predicate predicateConfirmPerson = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%");
                    predicateFilters.add(predicateConfirmPerson);
                }

                // 26
                if (!isEmpty(filterRequest.getApplicant())) {
                    Predicate predicateApplicantName = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getApplicant() + "%");
                    Predicate predicateApplicantStep = criteriaBuilder.equal(approvalFlowDetailsEntitiesDefault.get("stepNumber"), 1);
                    predicateFilters.add(criteriaBuilder.and(predicateApplicantName, predicateApplicantStep));
                }

                if (!isEmpty(filterRequest.getApprover())) {
                    // 27
                    Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesApprover = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                    CriteriaBuilder.In<Integer> inClauseApprover = criteriaBuilder.in(approvalFlowDetailsEntitiesApprover.get("stepNumber"));
                    inClauseApprover.value(2);
                    inClauseApprover.value(3);
                    inClauseApprover.value(4);
                    inClauseApprover.value(5);
                    approvalFlowDetailsEntitiesApprover.on(inClauseApprover);

                    Predicate predicateApprover = criteriaBuilder.like(approvalFlowDetailsEntitiesApprover.get("shainNm"), "%" + filterRequest.getApprover() + "%");
                    predicateFilters.add(predicateApprover);
                }

            }

            predicateFilters.add(predicateSendBackDefault);
            predicateFilters.add(criteriaBuilder.equal(root.get("isSentBack"), 1));
            Predicate[] predicateFilterArray = predicateFilters.toArray(new Predicate[predicateFilters.size()]);

            return criteriaBuilder.and(predicateFilterArray);
        };
    }

    public static Specification<RequestsEntity> filterRequestReject(String shainCd, FilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            // requests.status_cd=5
            Join<RequestsEntity, MstRequestStatusesEntity> mstRequestStatusesEntity = root.join("mstRequestStatusesEntity", JoinType.INNER);
            Predicate predicateMstRequestStatusesEntity = criteriaBuilder.equal(mstRequestStatusesEntity.get("name"), MST_REQUEST_STATUSES_NAME.get(4));

            Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesDefault = root.join("approvalFlowDetailsEntities", JoinType.INNER);
            Join<ApprovalFlowDetailsEntity, ShainEntity> shainEntityApprovalFlowDetail = approvalFlowDetailsEntitiesDefault.join("shainEntity", JoinType.INNER);

            Predicate predicateShainCdDefault = criteriaBuilder.equal(shainEntityApprovalFlowDetail.get("shainCd"), shainCd);

            Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.LEFT);
            Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT);
            Join<RequestAccountsReceivableDetailsEntity, ShainEntity> shainEntityReceivableDetail = requestAccountsReceivableDetailsEntities.join("shainEntity", JoinType.LEFT);

            Predicate predicateRequestAccountsReceivableDetail = criteriaBuilder.equal(shainEntityReceivableDetail.get("shainCd"), shainCd);

            // operation_histories.mst_request_comment_action_cd in (1,2) and
            // operation_histories.shain_cd={login user’s shain_cd}
            Join<RequestsEntity, OperationHistoriesEntity> operationHistoriesEntities = root.join("operationHistoriesEntities", JoinType.INNER);

            Join<OperationHistoriesEntity, MstRequestCommentActionsEntity> mstRequestCommentActionsEntity = operationHistoriesEntities.join("mstRequestCommentActionsEntity", JoinType.INNER);

            CriteriaBuilder.In<String> inClauseRequestCommentAction = criteriaBuilder.in(mstRequestCommentActionsEntity.get("name"));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(0));
            inClauseRequestCommentAction.value(MST_REQUEST_COMMENT_ACTIONS_NAME.get(1));

            Join<OperationHistoriesEntity, ShainEntity> shainEntityOperationHistory = operationHistoriesEntities.join("shainEntity", JoinType.INNER);
            Predicate predicateOperationHistory = criteriaBuilder.equal(shainEntityOperationHistory.get("shainCd"), shainCd);

            Predicate predicateRejectDefault = criteriaBuilder.and(predicateMstRequestStatusesEntity,
                    criteriaBuilder.or(predicateShainCdDefault, predicateRequestAccountsReceivableDetail,
                            criteriaBuilder.and(inClauseRequestCommentAction, predicateOperationHistory)));

            List<Predicate> predicateFilters = new ArrayList<>();

            if (!ObjectUtils.isEmpty(filterRequest)) {
                // 25
                if (!isEmpty(filterRequest.getConfirmPerson())) {

                    Predicate predicateConfirmPerson = criteriaBuilder.like(requestAccountsReceivableDetailsEntities.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%");
                    predicateFilters.add(predicateConfirmPerson);
                }

                // 26
                if (!isEmpty(filterRequest.getApplicant())) {
                    Predicate predicateApplicantName = criteriaBuilder.like(approvalFlowDetailsEntitiesDefault.get("shainNm"), "%" + filterRequest.getApplicant() + "%");
                    Predicate predicateApplicantStep = criteriaBuilder.equal(approvalFlowDetailsEntitiesDefault.get("stepNumber"), 1);
                    predicateFilters.add(criteriaBuilder.and(predicateApplicantName, predicateApplicantStep));
                }

                if (!isEmpty(filterRequest.getApprover())) {
                    // 27
                    Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntitiesApprover = root.join("approvalFlowDetailsEntities", JoinType.INNER);
                    CriteriaBuilder.In<Integer> inClauseApprover = criteriaBuilder.in(approvalFlowDetailsEntitiesApprover.get("stepNumber"));
                    inClauseApprover.value(2);
                    inClauseApprover.value(3);
                    inClauseApprover.value(4);
                    inClauseApprover.value(5);
                    approvalFlowDetailsEntitiesApprover.on(inClauseApprover);

                    Predicate predicateApprover = criteriaBuilder.like(approvalFlowDetailsEntitiesApprover.get("shainNm"), "%" + filterRequest.getApprover() + "%");
                    predicateFilters.add(predicateApprover);
                }

            }

            predicateFilters.add(predicateRejectDefault);
            Predicate[] predicateFilterArray = predicateFilters.toArray(new Predicate[predicateFilters.size()]);

            return criteriaBuilder.and(predicateFilterArray);
        };
    }

    // requests left join request_exhibition_promotions on requests.cd = request_exhibition_promotions.request_cd
    public static Specification<RequestsEntity> hasRequestExhibitionPromotions() {

        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, RequestExhibitionPromotionsEntity> requestExhibitionPromotionsEntites = root.join("requestExhibitionPromotionsEntities", JoinType.LEFT);

            return requestExhibitionPromotionsEntites.getOn();
        };
    }

    // requests left join request_mannequin_promotions on requests.cd = request_mannequin_promotions.request_cd
    public static Specification<RequestsEntity> hasRequestMannequinPromotions() {

        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, RequestMannequinPromotionsEntity> requestMannequinPromotionsEntities = root.join("requestMannequinPromotionsEntities", JoinType.LEFT);

            return requestMannequinPromotionsEntities.getOn();
        };
    }

    // Condition search
    // 21. FilterRequest.applyNumber
    // request_numbers.request_cd like applyNumber%
    public static Specification<RequestsEntity> hasApplyNumber(String applyNumber) {

        if (isEmpty(applyNumber)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {

            Join<RequestsEntity, RequestNumbersEntity> requestNumbersEntities = root.join("requestNumbersEntities", JoinType.INNER);
            Expression<String> expression = requestNumbersEntities.get("cd").as(String.class);

            return criteriaBuilder.like(expression, applyNumber + "%");
        };
    }

    // 22. FilterRequest.settlementNumber
    // requests.settlement_number like settlementNumber + "%"
    public static Specification<RequestsEntity> hasSettlementNumber(String settlementNumber) {

        if (isEmpty(settlementNumber)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("settlementNumber"), settlementNumber + "%");
    }

    // 23. FilterRequest.requestType
    // requests.mst_request_type_cd equal requestType
    public static Specification<RequestsEntity> hasRequestType(String requestType) {

        if (isEmpty(requestType)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, MstRequestTypesEntity> mstRequestTypesEntity = root.join("mstRequestTypesEntity", JoinType.INNER);

            return criteriaBuilder.equal(mstRequestTypesEntity.get("name"), requestType);
        };
    }

    // 29.
    // requests.torihiki_nm like %supplierName%
    public static Specification<RequestsEntity> hasSupplierName(String supplierName) {

        if (isEmpty(supplierName)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("torihikiNm"), "%" + supplierName + "%");
    }

    // 30.
    // case 0: appliedAtMin < requests.requested_at < appliedAtMax
    // case 1: appliedAtMin < requests.requested_at
    // case 2: requests.requested_at < appliedAtMax
    // default: null
    public static Specification<RequestsEntity> hasAppliedAt(java.util.Date appliedAtMinDate, java.util.Date appliedAtMaxDate) {

        LocalDateTime appliedAtMin = !ObjectUtils.isEmpty(appliedAtMinDate) ? appliedAtMinDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
        LocalDateTime appliedAtMax = !ObjectUtils.isEmpty(appliedAtMaxDate) ? appliedAtMaxDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        if (!ObjectUtils.isEmpty(appliedAtMin) && ObjectUtils.isEmpty(appliedAtMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("requestedAt"), appliedAtMin);
        } else if (ObjectUtils.isEmpty(appliedAtMin) && !ObjectUtils.isEmpty(appliedAtMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("requestedAt"), appliedAtMax);
        } else if (!ObjectUtils.isEmpty(appliedAtMin) && !ObjectUtils.isEmpty(appliedAtMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("requestedAt"), appliedAtMin, appliedAtMax);
        }

        return null;
    }

    // 31.
    // case 0: paymentScheduledDateMin < requests.scheduled_payment_on < paymentScheduledDateMax
    // case 1: paymentScheduledDateMin < requests.scheduled_payment_on
    // case 2: requests.scheduled_payment_on < paymentScheduledDateMax
    // default: null
    public static Specification<RequestsEntity> hasPaymentScheduledDate(Date paymentScheduledDateMin, Date paymentScheduledDateMax) {

        if (!ObjectUtils.isEmpty(paymentScheduledDateMin) && ObjectUtils.isEmpty(paymentScheduledDateMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("scheduledPaymentOn"), paymentScheduledDateMin);
        } else if (ObjectUtils.isEmpty(paymentScheduledDateMin) && !ObjectUtils.isEmpty(paymentScheduledDateMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("scheduledPaymentOn"), paymentScheduledDateMax);
        } else if (!ObjectUtils.isEmpty(paymentScheduledDateMin) && !ObjectUtils.isEmpty(paymentScheduledDateMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("scheduledPaymentOn"), paymentScheduledDateMin, paymentScheduledDateMax);
        }

        return null;
    }

    // 32.
    public static Specification<RequestsEntity> hasNumberOfStagnancyDay(Integer numberOfStagnancyDay) {

        if (ObjectUtils.isEmpty(numberOfStagnancyDay)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Period periodDay = Period.ofDays(numberOfStagnancyDay);
            LocalDateTime subDateNow = LocalDateTime.now().minus(periodDay);

            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedStatusAt"), subDateNow);
        };
    }

    // 33.
    public static Specification<RequestsEntity> hasAmount(Long amountMin, Long amountMax) {

        if (!ObjectUtils.isEmpty(amountMin) && ObjectUtils.isEmpty(amountMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("billingAmount"), amountMin);
        } else if (ObjectUtils.isEmpty(amountMin) && !ObjectUtils.isEmpty(amountMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("billingAmount"), amountMax);
        } else if (!ObjectUtils.isEmpty(amountMin) && !ObjectUtils.isEmpty(amountMax)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("billingAmount"), amountMin, amountMax);
        }

        return null;
    }

    // Create - List - Display
    public static Specification<RequestsEntity> requestListCreate(String shainCd) {
        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, OperationHistoriesEntity> operationHistoriesEntities = root.join("operationHistoriesEntities", JoinType.INNER);

            // operation_histories.mst_request_comment_action_cd=1
            Join<OperationHistoriesEntity, MstRequestCommentActionsEntity> mstRequestCommentActionsEntity = operationHistoriesEntities.join("mstRequestCommentActionsEntity", JoinType.INNER);
            Predicate predicateMstRequestCommentActions = criteriaBuilder.equal(mstRequestCommentActionsEntity.get("name"), MST_REQUEST_COMMENT_ACTIONS_NAME.get(0));

            // operation_histories.shain_cd={login user’s shain_cd}
            Join<OperationHistoriesEntity, ShainEntity> shainEntity = operationHistoriesEntities.join("shainEntity", JoinType.INNER);
            Predicate predicateShain = criteriaBuilder.equal(shainEntity.get("shainCd"), shainCd);

            return criteriaBuilder.and(predicateMstRequestCommentActions, predicateShain);
        };
    }

    // Confirm - List - Display
    public static Specification<RequestsEntity> requestListConfirm(String shainCd) {
        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = root.join("requestAccountsReceivablesEntities", JoinType.INNER);
            Join<RequestAccountsReceivablesEntity, RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntities.join("requestAccountsReceivableDetailsEntities", JoinType.INNER);
            Join<RequestAccountsReceivableDetailsEntity, ShainEntity> shainEntity = requestAccountsReceivableDetailsEntities.join("shainEntity", JoinType.INNER);

            // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
            Predicate predicateShain = criteriaBuilder.equal(shainEntity.get("shainCd"), shainCd);

            // request_accounts_receivable_details.is_checked=0
            Predicate predicateRequestAccountsReceivableDetail = criteriaBuilder.equal(requestAccountsReceivableDetailsEntities.get("isChecked"), 0);

            return criteriaBuilder.and(predicateShain, predicateRequestAccountsReceivableDetail);
        };
    }

    // Apply - List - Display or Approve - List - Display
    public static Specification<RequestsEntity> requestListApplyOrApprove(String shainCd) {
        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, ApprovalFlowDetailsEntity> approvalFlowDetailsEntities = root.join("approvalFlowDetailsEntities", JoinType.INNER);
            Join<ApprovalFlowDetailsEntity, ShainEntity> shainEntity = approvalFlowDetailsEntities.join("shainEntity", JoinType.INNER);

            // approvalflow_details.step_number= requests.step_number
            Predicate predicateApprovalflowDetail = criteriaBuilder.equal(root.get("stepNumber"), approvalFlowDetailsEntities.get("stepNumber"));

            // approvalflow_details.shain_cd={login user’s shain_cd}
            Predicate predicateShainCd = criteriaBuilder.equal(shainEntity.get("shainCd"), shainCd);

            return criteriaBuilder.and(predicateApprovalflowDetail, predicateShainCd);
        };
    }

    /**
     * requests.status_cd=1, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(0)
     * requests.status_cd=2, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(1)
     * requests.status_cd=3, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(2)
     * requests.status_cd=4, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(3)
     * requests.status_cd=5, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(4)
     * requests.status_cd=6, mstRequestStatusName = MST_REQUEST_STATUSES_NAME.get(5)
     */
    public static Specification<RequestsEntity> hasStatusCd(String mstRequestStatusName) {
        return (root, query, criteriaBuilder) -> {
            Join<RequestsEntity, MstRequestStatusesEntity> mstRequestStatusesEntity = root.join("mstRequestStatusesEntity", JoinType.INNER);

            return criteriaBuilder.equal(mstRequestStatusesEntity.get("name"), mstRequestStatusName);
        };
    }




}
