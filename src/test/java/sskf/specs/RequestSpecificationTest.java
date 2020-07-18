package sskf.specs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.FilterRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static sskf.specs.RequestSpecification.MST_REQUEST_COMMENT_ACTIONS_NAME;
import static sskf.specs.RequestSpecification.MST_REQUEST_STATUSES_NAME;
import static sskf.specs.RequestSpecification.SEND_BACK;
import static sskf.specs.RequestSpecification.filterRequestAll;
import static sskf.specs.RequestSpecification.filterRequestInProgress;
import static sskf.specs.RequestSpecification.filterRequestReject;
import static sskf.specs.RequestSpecification.filterRequestSendBack;
import static sskf.specs.RequestSpecification.hasAmount;
import static sskf.specs.RequestSpecification.hasAppliedAt;
import static sskf.specs.RequestSpecification.hasApplyNumber;
import static sskf.specs.RequestSpecification.hasNotDeleted;
import static sskf.specs.RequestSpecification.hasNumberOfStagnancyDay;
import static sskf.specs.RequestSpecification.hasPaymentScheduledDate;
import static sskf.specs.RequestSpecification.hasRequestExhibitionPromotions;
import static sskf.specs.RequestSpecification.hasRequestMannequinPromotions;
import static sskf.specs.RequestSpecification.hasRequestType;
import static sskf.specs.RequestSpecification.hasSettlementNumber;
import static sskf.specs.RequestSpecification.hasStatusCd;
import static sskf.specs.RequestSpecification.hasSupplierName;
import static sskf.specs.RequestSpecification.requestListApplyOrApprove;
import static sskf.specs.RequestSpecification.requestListConfirm;
import static sskf.specs.RequestSpecification.requestListCreate;

@ExtendWith(SpringExtension.class)
public class RequestSpecificationTest {

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Mock
    private CriteriaQuery queryMock;

    @Mock
    private Root<RequestsEntity> rootMock;


    @Test
    public void hasIsDeleted_With_Success() {
        Path isDeletedPathMock = mock(Path.class);
        when(rootMock.get("isDeleted")).thenReturn(isDeletedPathMock);

        Predicate isDeletedPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.equal(rootMock.get("isDeleted"), 0)).thenReturn(isDeletedPredicateMock);

        Specification<RequestsEntity> actual = hasNotDeleted();
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(isDeletedPredicateMock);
    }

    @Test
    public void hasSettlementNumber_With_Success_1() {
        Path settlementNumberPathMock = mock(Path.class);
        when(rootMock.get("settlementNumber")).thenReturn(settlementNumberPathMock);

        Predicate settlementNumberPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(rootMock.get("settlementNumber"), "test%")).thenReturn(settlementNumberPredicateMock);

        Specification<RequestsEntity> actual = hasSettlementNumber("test");
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(settlementNumberPredicateMock);
    }

    @Test
    public void hasSettlementNumber_With_Success_2() {
        Specification<RequestsEntity> actual = hasSettlementNumber("");
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasSupplierName_With_Success_1() {
        Path supplierNamePathMock = mock(Path.class);
        when(rootMock.get("torihikiNm")).thenReturn(supplierNamePathMock);

        Predicate supplierNamePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(rootMock.get("torihikiNm"), "%test%")).thenReturn(supplierNamePredicateMock);

        Specification<RequestsEntity> actual = hasSupplierName("test");
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(supplierNamePredicateMock);
    }

    @Test
    public void hasSupplierName_With_Success_2() {
        Specification<RequestsEntity> actual = hasSupplierName("");
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasAppliedAt_With_Success_1() {
        Date min = new Date();
        LocalDateTime appliedAtMin = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Path appliedAtPathMock = mock(Path.class);
        when(rootMock.get("requestedAt")).thenReturn(appliedAtPathMock);

        Predicate appliedAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.greaterThan(rootMock.get("requestedAt"), appliedAtMin)).thenReturn(appliedAtPredicateMock);

        Specification<RequestsEntity> actual = hasAppliedAt(min, null);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(appliedAtPredicateMock);
    }

    @Test
    public void hasAppliedAt_With_Success_2() {
        Date max = new Date();
        LocalDateTime appliedAtMax = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Path appliedAtPathMock = mock(Path.class);
        when(rootMock.get("requestedAt")).thenReturn(appliedAtPathMock);

        Predicate appliedAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.lessThan(rootMock.get("requestedAt"), appliedAtMax)).thenReturn(appliedAtPredicateMock);

        Specification<RequestsEntity> actual = hasAppliedAt(null, max);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(appliedAtPredicateMock);
    }

    @Test
    public void hasAppliedAt_With_Success_3() {
        Date min = new Date();
        LocalDateTime appliedAtMin = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Date max = new Date();
        LocalDateTime appliedAtMax = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Path appliedAtPathMock = mock(Path.class);
        when(rootMock.get("requestedAt")).thenReturn(appliedAtPathMock);

        Predicate appliedAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.between(rootMock.get("requestedAt"), appliedAtMin, appliedAtMax)).thenReturn(appliedAtPredicateMock);

        Specification<RequestsEntity> actual = hasAppliedAt(min, max);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(appliedAtPredicateMock);
    }

    @Test
    public void hasAppliedAt_With_Success_4() {
        Specification<RequestsEntity> actual = hasAppliedAt(null, null);

        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasPaymentScheduledDate_With_Success_1() {
        java.sql.Date paymentScheduledDateMin = new java.sql.Date(new Date().getTime());

        Path paymentScheduledDatePathMock = mock(Path.class);
        when(rootMock.get("scheduledPaymentOn")).thenReturn(paymentScheduledDatePathMock);

        Predicate paymentScheduledDatePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.greaterThan(rootMock.get("scheduledPaymentOn"), paymentScheduledDateMin)).thenReturn(paymentScheduledDatePredicateMock);

        Specification<RequestsEntity> actual = hasPaymentScheduledDate(paymentScheduledDateMin, null);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(paymentScheduledDatePredicateMock);
    }

    @Test
    public void hasPaymentScheduledDate_With_Success_2() {
        java.sql.Date paymentScheduledDateMax = new java.sql.Date(new Date().getTime());

        Path paymentScheduledDatePathMock = mock(Path.class);
        when(rootMock.get("scheduledPaymentOn")).thenReturn(paymentScheduledDatePathMock);

        Predicate paymentScheduledDatePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.lessThan(rootMock.get("scheduledPaymentOn"), paymentScheduledDateMax)).thenReturn(paymentScheduledDatePredicateMock);

        Specification<RequestsEntity> actual = hasPaymentScheduledDate(null, paymentScheduledDateMax);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(paymentScheduledDatePredicateMock);
    }

    @Test
    public void hasPaymentScheduledDate_With_Success_3() {
        java.sql.Date paymentScheduledDateMin = new java.sql.Date(new Date().getTime());
        java.sql.Date paymentScheduledDateMax = new java.sql.Date(new Date().getTime());

        Path paymentScheduledDatePathMock = mock(Path.class);
        when(rootMock.get("scheduledPaymentOn")).thenReturn(paymentScheduledDatePathMock);

        Predicate paymentScheduledDatePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.between(rootMock.get("scheduledPaymentOn"), paymentScheduledDateMin, paymentScheduledDateMax)).thenReturn(paymentScheduledDatePredicateMock);

        Specification<RequestsEntity> actual = hasPaymentScheduledDate(paymentScheduledDateMin, paymentScheduledDateMax);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(paymentScheduledDatePredicateMock);
    }

    @Test
    public void hasPaymentScheduledDate_With_Success_4() {
        Specification<RequestsEntity> actual = hasPaymentScheduledDate(null, null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasNumberOfStagnancyDay_With_Success_1() {
        Integer numberOfStagnancyDay = 3;

        Path numberOfStagnancyDayPathMock = mock(Path.class);
        when(rootMock.get("updatedStatusAt")).thenReturn(numberOfStagnancyDayPathMock);

        Predicate numberOfStagnancyDayPredicateMock = mock(Predicate.class);
        Period periodDay = Period.ofDays(numberOfStagnancyDay);
        LocalDateTime subDateNow = LocalDateTime.now().minus(periodDay);
        when(criteriaBuilderMock.lessThanOrEqualTo(rootMock.get("updatedStatusAt"), subDateNow)).thenReturn(numberOfStagnancyDayPredicateMock);

        Specification<RequestsEntity> actual = hasNumberOfStagnancyDay(numberOfStagnancyDay);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);
        Assertions.assertThat(true);
    }

    @Test
    public void hasNumberOfStagnancyDay_With_Success_2() {
        Specification<RequestsEntity> actual = hasNumberOfStagnancyDay(null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasAmount_With_Success_1() {
        Long amountMin = Long.valueOf("999999999999");

        Path amountPathMock = mock(Path.class);
        when(rootMock.get("billingAmount")).thenReturn(amountPathMock);

        Predicate amountPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.greaterThan(rootMock.get("billingAmount"), amountMin)).thenReturn(amountPredicateMock);

        Specification<RequestsEntity> actual = hasAmount(amountMin, null);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(amountPredicateMock);
    }

    @Test
    public void hasAmount_With_Success_2() {
        Long amountMax = Long.valueOf("999999999999");

        Path amountPathMock = mock(Path.class);
        when(rootMock.get("billingAmount")).thenReturn(amountPathMock);

        Predicate amountPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.lessThan(rootMock.get("billingAmount"), amountMax)).thenReturn(amountPredicateMock);

        Specification<RequestsEntity> actual = hasAmount(null, amountMax);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(amountPredicateMock);
    }

    @Test
    public void hasAmount_With_Success_3() {
        Long amountMin = Long.valueOf("999999999999");
        Long amountMax = Long.valueOf("999999999999");

        Path amountPathMock = mock(Path.class);
        when(rootMock.get("billingAmount")).thenReturn(amountPathMock);

        Predicate amountPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.between(rootMock.get("billingAmount"), amountMin, amountMax)).thenReturn(amountPredicateMock);

        Specification<RequestsEntity> actual = hasAmount(amountMin, amountMax);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(amountPredicateMock);
    }

    @Test
    public void hasAmount_With_Success_4() {
        Specification<RequestsEntity> actual = hasAmount(null, null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void filterRequestInProgress_With_Success_1() {
        String shainCd = "118602";
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setConfirmPerson("confirm person");
        filterRequest.setApplicant("applicant");
        filterRequest.setApprover("approver");
        filterRequest.setStatus(SEND_BACK);

        // requests.status_cd in (1,2,3,4)
        Join mstRequestStatusesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestStatusesEntity", JoinType.INNER)).thenReturn(mstRequestStatusesEntityMock);
        when(mstRequestStatusesEntityMock.get("name")).thenReturn(mock(Path.class));
        CriteriaBuilder.In<String> inClauseStatusCdMock = mock(CriteriaBuilder.In.class);
        when(criteriaBuilderMock.in(mstRequestStatusesEntityMock.get("name"))).thenReturn(inClauseStatusCdMock);

        // approvalflow_details1.shain_cd={login user’s shain_cd}
        Join approvalFlowDetailsEntitiesDefaultMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesDefaultMock);
        Join shainEntityDefaultMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesDefaultMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityDefaultMock);

        Predicate predicateShainCdDefaultMock = mock(Predicate.class);
        when(shainEntityDefaultMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityDefaultMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdDefaultMock);

        // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivablesEntitiesMock);
        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);
        Join shainEntityReceivableDetailMock = mock(Join.class);
        when(requestAccountsReceivableDetailsEntitiesMock.join("shainEntity", JoinType.LEFT)).thenReturn(shainEntityReceivableDetailMock);

        Predicate predicateShainCdReceivableDetailMock = mock(Predicate.class);
        when(shainEntityReceivableDetailMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityReceivableDetailMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdReceivableDetailMock);

        // operation_histories.mst_request_comment_action_cd in (1,2) and
        // operation_histories.shain_cd={login user’s shain_cd}
        Join operationHistoriesEntitiesMock = mock(Join.class);
        when(rootMock.join("operationHistoriesEntities", JoinType.INNER)).thenReturn(operationHistoriesEntitiesMock);
        Join mstRequestCommentActionsEntityMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("mstRequestCommentActionsEntity", JoinType.INNER)).thenReturn(mstRequestCommentActionsEntityMock);

        CriteriaBuilder.In<String> inClauseRequestCommentActionMock = mock(CriteriaBuilder.In.class);
        when(mstRequestCommentActionsEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(mstRequestCommentActionsEntityMock.get("name"))).thenReturn(inClauseRequestCommentActionMock);

        Join shainEntityOperationHistoryMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityOperationHistoryMock);

        Predicate predicateOperationHistoryMock = mock(Predicate.class);
        when(shainEntityOperationHistoryMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityOperationHistoryMock.get("shainCd"), shainCd)).thenReturn(predicateOperationHistoryMock);

        Predicate predicateInProgressDefaultMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(inClauseStatusCdMock,
                criteriaBuilderMock.or(predicateShainCdDefaultMock, predicateShainCdReceivableDetailMock,
                        criteriaBuilderMock.and(inClauseRequestCommentActionMock, predicateOperationHistoryMock))))
                .thenReturn(predicateInProgressDefaultMock);

        // List predicate
        List<Predicate> predicateFiltersMock = new ArrayList<>();

        // 25
        Predicate predicateConfirmPersonMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%")).thenReturn(predicateConfirmPersonMock);
        predicateFiltersMock.add(predicateConfirmPersonMock);

        // 26
        Predicate predicateApplicantNameMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getApplicant() + "%")).thenReturn(predicateApplicantNameMock);
        Predicate predicateApplicantStepMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber"), 1)).thenReturn(predicateApplicantStepMock);

        Predicate predicateApplicantNameAndApplicantStepMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApplicantNameMock, predicateApplicantStepMock)).thenReturn(predicateApplicantNameAndApplicantStepMock);
        predicateFiltersMock.add(predicateApplicantNameAndApplicantStepMock);

        // 27
        CriteriaBuilder.In<Integer> inClauseApproverMock = mock(CriteriaBuilder.In.class);
        Join approvalFlowDetailsEntitiesApproverMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(approvalFlowDetailsEntitiesApproverMock.get("stepNumber"))).thenReturn(inClauseApproverMock);

        Join shainEntityApproverMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesApproverMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApproverMock);
        when(shainEntityApproverMock.get("shainCd")).thenReturn(mock(Path.class));
//        Predicate predicateShainCdApproverMock = mock(Predicate.class);
//
//        when(criteriaBuilderMock.equal(shainEntityApproverMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("shainNm")).thenReturn(mock(Path.class));
        Predicate predicateApproverMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesApproverMock.get("shainNm"), "%" + filterRequest.getApprover() + "%")).thenReturn(predicateApproverMock);
        predicateFiltersMock.add(predicateApproverMock);

//        Predicate predicateApproverAndShainCdApproverMock = mock(Predicate.class);
//        when(criteriaBuilderMock.and(predicateApproverMock, predicateShainCdApproverMock)).thenReturn(predicateApproverAndShainCdApproverMock);
//        predicateFiltersMock.add(predicateApproverAndShainCdApproverMock);

        // 28
        Predicate predicateIsSentBackMock = mock(Predicate.class);
        when(rootMock.get("isSentBack")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(rootMock.get("isSentBack"), 1)).thenReturn(predicateIsSentBackMock);
        predicateFiltersMock.add(predicateIsSentBackMock);


        Predicate predicateInProgressDefaultExpected = mock(Predicate.class);
        predicateFiltersMock.add(predicateInProgressDefaultMock);
        Predicate[] predicateFilterArrayMock = predicateFiltersMock.toArray(new Predicate[predicateFiltersMock.size()]);
        when(criteriaBuilderMock.and(predicateFilterArrayMock)).thenReturn(predicateInProgressDefaultExpected);

        Specification<RequestsEntity> actual = filterRequestInProgress(shainCd, filterRequest);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(true);
    }

    @Test
    public void hasRequestExhibitionPromotions_With_Success() {
        Join requestExhibitionPromotionsEntitesMock = mock(Join.class);
        when(rootMock.join("requestExhibitionPromotionsEntities", JoinType.LEFT)).thenReturn(requestExhibitionPromotionsEntitesMock);
        Predicate predicateExpected = requestExhibitionPromotionsEntitesMock.getOn();

        Specification<RequestsEntity> actual = hasRequestExhibitionPromotions();
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void hasRequestMannequinPromotions_With_Success() {
        Join requestMannequinPromotionsEntitiesMock = mock(Join.class);
        when(rootMock.join("requestMannequinPromotionsEntities", JoinType.LEFT)).thenReturn(requestMannequinPromotionsEntitiesMock);
        Predicate predicateExpected = requestMannequinPromotionsEntitiesMock.getOn();

        Specification<RequestsEntity> actual = hasRequestMannequinPromotions();
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void hasApplyNumber_With_Success_1() {
        String applyNumber = "000001";

        Join requestNumbersEntitiesMock = mock(Join.class);
        when(rootMock.join("requestNumbersEntities", JoinType.INNER)).thenReturn(requestNumbersEntitiesMock);
        Expression<String> expression = mock(Expression.class);
        when(requestNumbersEntitiesMock.get("cd")).thenReturn(mock(Path.class));
        when(requestNumbersEntitiesMock.get("cd").as(String.class)).thenReturn(expression);

        Predicate predicateExpected = mock(Predicate.class);
        when(criteriaBuilderMock.like(expression, applyNumber + "%")).thenReturn(predicateExpected);
        Specification<RequestsEntity> actual = hasApplyNumber(applyNumber);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void hasApplyNumber_With_Success_2() {
        Specification<RequestsEntity> actual = hasApplyNumber(null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasRequestType_With_Success_1() {
        String requestType = "type_1";
        Join mstRequestTypesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestTypesEntity", JoinType.INNER)).thenReturn(mstRequestTypesEntityMock);

        Predicate predicateExpected = mock(Predicate.class);
        when(mstRequestTypesEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(mstRequestTypesEntityMock.get("name"), requestType)).thenReturn(predicateExpected);

        Specification<RequestsEntity> actual = hasRequestType(requestType);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void hasRequestType_With_Success_2() {
        Specification<RequestsEntity> actual = hasRequestType(null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void requestListCreate_With_Success_1() {
        String shainCd = "118602";

        Join operationHistoriesEntitiesMock = mock(Join.class);
        when(rootMock.join("operationHistoriesEntities", JoinType.INNER)).thenReturn(operationHistoriesEntitiesMock);

        Join mstRequestCommentActionsEntityMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("mstRequestCommentActionsEntity", JoinType.INNER)).thenReturn(mstRequestCommentActionsEntityMock);

        Predicate predicateMstRequestCommentActionsMock = mock(Predicate.class);
        when(mstRequestCommentActionsEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(mstRequestCommentActionsEntityMock.get("name"), MST_REQUEST_COMMENT_ACTIONS_NAME.get(0))).thenReturn(predicateMstRequestCommentActionsMock);

        Join shainEntityMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityMock);

        Predicate predicateShainMock = mock(Predicate.class);
        when(shainEntityMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityMock.get("shainCd"), shainCd)).thenReturn(predicateShainMock);

        Predicate predicateExpected = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateMstRequestCommentActionsMock, predicateShainMock)).thenReturn(predicateExpected);

        Specification<RequestsEntity> actual = requestListCreate(shainCd);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void requestListConfirm_With_Success_1() {
        String shainCd = "118602";

        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.INNER)).thenReturn(requestAccountsReceivablesEntitiesMock);

        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.INNER)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);

        Join shainEntityMock = mock(Join.class);
        when(requestAccountsReceivableDetailsEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityMock);

        Predicate predicateShainMock = mock(Predicate.class);
        when(shainEntityMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityMock.get("shainCd"), shainCd)).thenReturn(predicateShainMock);

        Predicate predicateRequestAccountsReceivableDetailMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("isChecked")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(requestAccountsReceivableDetailsEntitiesMock.get("isChecked"), 0)).thenReturn(predicateRequestAccountsReceivableDetailMock);

        Predicate predicateExpected = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateShainMock, predicateRequestAccountsReceivableDetailMock)).thenReturn(predicateExpected);

        Specification<RequestsEntity> actual = requestListConfirm(shainCd);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void hasStatusCd_With_Success() {
        Join mstRequestStatusesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestStatusesEntity", JoinType.INNER)).thenReturn(mstRequestStatusesEntityMock);

        Predicate predicateExpected = mock(Predicate.class);
        when(mstRequestStatusesEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(mstRequestStatusesEntityMock.get("name"), MST_REQUEST_STATUSES_NAME.get(0))).thenReturn(predicateExpected);

        Specification<RequestsEntity> actual = hasStatusCd(MST_REQUEST_STATUSES_NAME.get(0));
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpected);
    }

    @Test
    public void filterRequestReject_With_Success() {
        String shainCd = "118602";
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setConfirmPerson("confirm person");
        filterRequest.setApplicant("applicant");
        filterRequest.setApprover("approver");

        // requests.status_cd=5
        Join mstRequestStatusesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestStatusesEntity", JoinType.INNER)).thenReturn(mstRequestStatusesEntityMock);
        Predicate predicateMstRequestStatusesEntityMock = mock(Predicate.class);
        when(mstRequestStatusesEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(mstRequestStatusesEntityMock.get("name"), MST_REQUEST_STATUSES_NAME.get(4))).thenReturn(predicateMstRequestStatusesEntityMock);

        // approvalflow_details1.shain_cd={login user’s shain_cd}
        Join approvalFlowDetailsEntitiesDefaultMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesDefaultMock);
        Join shainEntityApprovalFlowDetailMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesDefaultMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApprovalFlowDetailMock);

        Predicate predicateShainCdDefaultMock = mock(Predicate.class);
        when(shainEntityApprovalFlowDetailMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityApprovalFlowDetailMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdDefaultMock);

        // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivablesEntitiesMock);
        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);
        Join shainEntityReceivableDetailMock = mock(Join.class);
        when(requestAccountsReceivableDetailsEntitiesMock.join("shainEntity", JoinType.LEFT)).thenReturn(shainEntityReceivableDetailMock);

        Predicate predicateRequestAccountsReceivableDetailMock = mock(Predicate.class);
        when(shainEntityReceivableDetailMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityReceivableDetailMock.get("shainCd"), shainCd)).thenReturn(predicateRequestAccountsReceivableDetailMock);

        // operation_histories.mst_request_comment_action_cd in (1,2) and
        // operation_histories.shain_cd={login user’s shain_cd}
        Join operationHistoriesEntitiesMock = mock(Join.class);
        when(rootMock.join("operationHistoriesEntities", JoinType.INNER)).thenReturn(operationHistoriesEntitiesMock);
        Join mstRequestCommentActionsEntityMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("mstRequestCommentActionsEntity", JoinType.INNER)).thenReturn(mstRequestCommentActionsEntityMock);

        CriteriaBuilder.In<String> inClauseRequestCommentActionMock = mock(CriteriaBuilder.In.class);
        when(mstRequestCommentActionsEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(mstRequestCommentActionsEntityMock.get("name"))).thenReturn(inClauseRequestCommentActionMock);

        Join shainEntityOperationHistoryMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityOperationHistoryMock);

        Predicate predicateOperationHistoryMock = mock(Predicate.class);
        when(shainEntityOperationHistoryMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityOperationHistoryMock.get("shainCd"), shainCd)).thenReturn(predicateOperationHistoryMock);

        Predicate predicateRejectDefaultMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateMstRequestStatusesEntityMock,
                criteriaBuilderMock.or(predicateShainCdDefaultMock, predicateRequestAccountsReceivableDetailMock,
                        criteriaBuilderMock.and(inClauseRequestCommentActionMock, predicateOperationHistoryMock))))
                .thenReturn(predicateRejectDefaultMock);

        List<Predicate> predicateFiltersMock = new ArrayList<>();

        // 25
        Predicate predicateConfirmPersonMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%")).thenReturn(predicateConfirmPersonMock);
        predicateFiltersMock.add(predicateConfirmPersonMock);

        // 26
        Predicate predicateApplicantNameMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getApplicant() + "%")).thenReturn(predicateApplicantNameMock);
        Predicate predicateApplicantStepMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber"), 1)).thenReturn(predicateApplicantStepMock);

        Predicate predicateApplicantNameAndApplicantStepMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApplicantNameMock, predicateApplicantStepMock)).thenReturn(predicateApplicantNameAndApplicantStepMock);
        predicateFiltersMock.add(predicateApplicantNameAndApplicantStepMock);

        // 27
        CriteriaBuilder.In<Integer> inClauseApproverMock = mock(CriteriaBuilder.In.class);
        Join approvalFlowDetailsEntitiesApproverMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(approvalFlowDetailsEntitiesApproverMock.get("stepNumber"))).thenReturn(inClauseApproverMock);

        Join shainEntityApproverMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesApproverMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApproverMock);
        when(shainEntityApproverMock.get("shainCd")).thenReturn(mock(Path.class));
        //Predicate predicateShainCdApproverMock = mock(Predicate.class);

        //when(criteriaBuilderMock.equal(shainEntityApproverMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("shainNm")).thenReturn(mock(Path.class));
        Predicate predicateApproverMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesApproverMock.get("shainNm"), "%" + filterRequest.getApprover() + "%")).thenReturn(predicateApproverMock);
        predicateFiltersMock.add(predicateApproverMock);

        //Predicate predicateApproverAndShainCdApproverMock = mock(Predicate.class);
        //when(criteriaBuilderMock.and(predicateApproverMock, predicateShainCdApproverMock)).thenReturn(predicateApproverAndShainCdApproverMock);
        //predicateFiltersMock.add(predicateApproverAndShainCdApproverMock);

        Predicate predicateRejectDefaultExpected = mock(Predicate.class);
        predicateFiltersMock.add(predicateRejectDefaultMock);
        Predicate[] predicateFilterArrayMock = predicateFiltersMock.toArray(new Predicate[predicateFiltersMock.size()]);
        when(criteriaBuilderMock.and(predicateFilterArrayMock)).thenReturn(predicateRejectDefaultExpected);

        Specification<RequestsEntity> actual = filterRequestReject(shainCd, filterRequest);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(true);
        //Assertions.assertThat(actualPredicate).isEqualTo(predicateRejectDefaultExpected);
    }

    @Test
    public void filterRequestSendBack_With_Success() {
        String shainCd = "118602";
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setConfirmPerson("confirm person");
        filterRequest.setApplicant("applicant");
        filterRequest.setApprover("approver");

        // approvalflow_details1.shain_cd={login user’s shain_cd}
        Join approvalFlowDetailsEntitiesDefaultMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesDefaultMock);
        Join shainEntityApprovalFlowDetailMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesDefaultMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApprovalFlowDetailMock);

        Predicate predicateShainCdDefaultMock = mock(Predicate.class);
        when(shainEntityApprovalFlowDetailMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityApprovalFlowDetailMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdDefaultMock);

        // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivablesEntitiesMock);
        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);
        Join shainEntityReceivableDetailMock = mock(Join.class);
        when(requestAccountsReceivableDetailsEntitiesMock.join("shainEntity", JoinType.LEFT)).thenReturn(shainEntityReceivableDetailMock);

        Predicate predicateShainCdReceivableDetailMock = mock(Predicate.class);
        when(shainEntityReceivableDetailMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityReceivableDetailMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdReceivableDetailMock);


        // operation_histories.mst_request_comment_action_cd in (1,2) and
        // operation_histories.shain_cd={login user’s shain_cd}
        Join operationHistoriesEntitiesMock = mock(Join.class);
        when(rootMock.join("operationHistoriesEntities", JoinType.INNER)).thenReturn(operationHistoriesEntitiesMock);
        Join mstRequestCommentActionsEntityMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("mstRequestCommentActionsEntity", JoinType.INNER)).thenReturn(mstRequestCommentActionsEntityMock);

        CriteriaBuilder.In<String> inClauseRequestCommentActionMock = mock(CriteriaBuilder.In.class);
        when(mstRequestCommentActionsEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(mstRequestCommentActionsEntityMock.get("name"))).thenReturn(inClauseRequestCommentActionMock);

        Join shainEntityOperationHistoryMock = mock(Join.class);
        when(operationHistoriesEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityOperationHistoryMock);

        Predicate predicateOperationHistoryMock = mock(Predicate.class);
        when(shainEntityOperationHistoryMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityOperationHistoryMock.get("shainCd"), shainCd)).thenReturn(predicateOperationHistoryMock);

        Predicate predicateRejectDefaultMock = mock(Predicate.class);
        when(criteriaBuilderMock.or(predicateShainCdDefaultMock, predicateShainCdReceivableDetailMock,
                criteriaBuilderMock.and(inClauseRequestCommentActionMock, predicateOperationHistoryMock)))
                .thenReturn(predicateRejectDefaultMock);

        List<Predicate> predicateFiltersMock = new ArrayList<>();

        // 25
        Predicate predicateConfirmPersonMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%")).thenReturn(predicateConfirmPersonMock);
        predicateFiltersMock.add(predicateConfirmPersonMock);

        // 26
        Predicate predicateApplicantNameMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getApplicant() + "%")).thenReturn(predicateApplicantNameMock);
        Predicate predicateApplicantStepMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber"), 1)).thenReturn(predicateApplicantStepMock);

        Predicate predicateApplicantNameAndApplicantStepMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApplicantNameMock, predicateApplicantStepMock)).thenReturn(predicateApplicantNameAndApplicantStepMock);
        predicateFiltersMock.add(predicateApplicantNameAndApplicantStepMock);

        // 27
        CriteriaBuilder.In<Integer> inClauseApproverMock = mock(CriteriaBuilder.In.class);
        Join approvalFlowDetailsEntitiesApproverMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(approvalFlowDetailsEntitiesApproverMock.get("stepNumber"))).thenReturn(inClauseApproverMock);

        Join shainEntityApproverMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesApproverMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApproverMock);
        when(shainEntityApproverMock.get("shainCd")).thenReturn(mock(Path.class));
        //Predicate predicateShainCdApproverMock = mock(Predicate.class);

        //when(criteriaBuilderMock.equal(shainEntityApproverMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("shainNm")).thenReturn(mock(Path.class));
        Predicate predicateApproverMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesApproverMock.get("shainNm"), "%" + filterRequest.getApprover() + "%")).thenReturn(predicateApproverMock);

        predicateFiltersMock.add(predicateApproverMock);
        //Predicate predicateApproverAndShainCdApproverMock = mock(Predicate.class);
        //when(criteriaBuilderMock.and(predicateApproverMock, predicateShainCdApproverMock)).thenReturn(predicateApproverAndShainCdApproverMock);
        //predicateFiltersMock.add(predicateApproverAndShainCdApproverMock);

        Predicate predicateRejectDefaultExpected = mock(Predicate.class);
        predicateFiltersMock.add(predicateRejectDefaultMock);
        Predicate[] predicateFilterArrayMock = predicateFiltersMock.toArray(new Predicate[predicateFiltersMock.size()]);
        when(criteriaBuilderMock.and(predicateFilterArrayMock)).thenReturn(predicateRejectDefaultExpected);

        Specification<RequestsEntity> actual = filterRequestSendBack(shainCd, filterRequest);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(true);
        // Assertions.assertThat(actualPredicate).isEqualTo(predicateRejectDefaultExpected);
    }

    @Test
    public void filterRequestAll_With_Success_1() {
        String shainCd = "118602";
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setRelatedPerson("related person");
        filterRequest.setConfirmPerson("confirm person");
        filterRequest.setApplicant("applicant");
        filterRequest.setApprover("approver");
        filterRequest.setStatus(MST_REQUEST_STATUSES_NAME.get(0));

        List<Predicate> predicateFiltersMock = new ArrayList<>();

        // 24
        // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivablesEntitiesMock);
        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));

        Predicate predicateRelatedPersonRequestAccountsReceivableDetailMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getRelatedPerson() + "%")).thenReturn(predicateRelatedPersonRequestAccountsReceivableDetailMock);

        Join approvalFlowDetailsEntitiesDefaultMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesDefaultMock);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));

        Predicate predicateRelatedPersonApprovalFlowDetailMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getRelatedPerson() + "%")).thenReturn(predicateRelatedPersonApprovalFlowDetailMock);

        Predicate predicateRelatedPersonMock = mock(Predicate.class);
        when(criteriaBuilderMock.or(predicateRelatedPersonRequestAccountsReceivableDetailMock, predicateRelatedPersonApprovalFlowDetailMock)).thenReturn(predicateRelatedPersonMock);

        predicateFiltersMock.add(predicateRelatedPersonMock);

        // 25
        Predicate predicateConfirmPersonMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%")).thenReturn(predicateConfirmPersonMock);
        predicateFiltersMock.add(predicateConfirmPersonMock);

        // 26
        Predicate predicateApplicantNameMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getApplicant() + "%")).thenReturn(predicateApplicantNameMock);
        Predicate predicateApplicantStepMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber"), 1)).thenReturn(predicateApplicantStepMock);

        Predicate predicateApplicantNameAndApplicantStepMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApplicantNameMock, predicateApplicantStepMock)).thenReturn(predicateApplicantNameAndApplicantStepMock);
        predicateFiltersMock.add(predicateApplicantNameAndApplicantStepMock);

        // 27
        CriteriaBuilder.In<Integer> inClauseApproverMock = mock(CriteriaBuilder.In.class);
        Join approvalFlowDetailsEntitiesApproverMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(approvalFlowDetailsEntitiesApproverMock.get("stepNumber"))).thenReturn(inClauseApproverMock);

        Join shainEntityApproverMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesApproverMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApproverMock);
        when(shainEntityApproverMock.get("shainCd")).thenReturn(mock(Path.class));
        //Predicate predicateShainCdApproverMock = mock(Predicate.class);

        //when(criteriaBuilderMock.equal(shainEntityApproverMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("shainNm")).thenReturn(mock(Path.class));
        Predicate predicateApproverMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesApproverMock.get("shainNm"), "%" + filterRequest.getApprover() + "%")).thenReturn(predicateApproverMock);
        predicateFiltersMock.add(predicateApproverMock);

        //Predicate predicateApproverAndShainCdApproverMock = mock(Predicate.class);
        //when(criteriaBuilderMock.and(predicateApproverMock, predicateShainCdApproverMock)).thenReturn(predicateApproverAndShainCdApproverMock);
        //predicateFiltersMock.add(predicateApproverAndShainCdApproverMock);

        // 28
        Join mstRequestStatusesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestStatusesEntity", JoinType.INNER)).thenReturn(mstRequestStatusesEntityMock);
        Predicate predicateName = mock(Predicate.class);
        when(mstRequestStatusesEntityMock.get("name")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(mstRequestStatusesEntityMock.get("name"), filterRequest.getStatus())).thenReturn(predicateName);
        predicateFiltersMock.add(predicateName);

        Predicate predicateRejectDefaultExpected = mock(Predicate.class);
        Predicate[] predicateFilterArrayMock = predicateFiltersMock.toArray(new Predicate[predicateFiltersMock.size()]);
        when(criteriaBuilderMock.and(predicateFilterArrayMock)).thenReturn(predicateRejectDefaultExpected);

        Specification<RequestsEntity> actual = filterRequestAll(shainCd, filterRequest);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(true);
    }

    @Test
    public void filterRequestAll_With_Success_2() {
        String shainCd = "118602";
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setConfirmPerson("confirm person");
        filterRequest.setApplicant("applicant");
        filterRequest.setApprover("approver");
        filterRequest.setStatus(SEND_BACK);

        List<Predicate> predicateFiltersMock = new ArrayList<>();

        // 25
        // request_accounts_receivable_details.shain_cd={login user’s shain_cd}
        Join requestAccountsReceivablesEntitiesMock = mock(Join.class);
        when(rootMock.join("requestAccountsReceivablesEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivablesEntitiesMock);
        Join requestAccountsReceivableDetailsEntitiesMock = mock(Join.class);
        when(requestAccountsReceivablesEntitiesMock.join("requestAccountsReceivableDetailsEntities", JoinType.LEFT)).thenReturn(requestAccountsReceivableDetailsEntitiesMock);
        Predicate predicateConfirmPersonMock = mock(Predicate.class);
        when(requestAccountsReceivableDetailsEntitiesMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(requestAccountsReceivableDetailsEntitiesMock.get("shainNm"), "%" + filterRequest.getConfirmPerson() + "%")).thenReturn(predicateConfirmPersonMock);
        predicateFiltersMock.add(predicateConfirmPersonMock);

        // 26
        Join approvalFlowDetailsEntitiesDefaultMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesDefaultMock);

        Predicate predicateApplicantNameMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("shainNm")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesDefaultMock.get("shainNm"), "%" + filterRequest.getApplicant() + "%")).thenReturn(predicateApplicantNameMock);
        Predicate predicateApplicantStepMock = mock(Predicate.class);
        when(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(approvalFlowDetailsEntitiesDefaultMock.get("stepNumber"), 1)).thenReturn(predicateApplicantStepMock);

        Predicate predicateApplicantNameAndApplicantStepMock = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApplicantNameMock, predicateApplicantStepMock)).thenReturn(predicateApplicantNameAndApplicantStepMock);
        predicateFiltersMock.add(predicateApplicantNameAndApplicantStepMock);

        // 27
        CriteriaBuilder.In<Integer> inClauseApproverMock = mock(CriteriaBuilder.In.class);
        Join approvalFlowDetailsEntitiesApproverMock = mock(Join.class);
        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.in(approvalFlowDetailsEntitiesApproverMock.get("stepNumber"))).thenReturn(inClauseApproverMock);

        Join shainEntityApproverMock = mock(Join.class);
        when(approvalFlowDetailsEntitiesApproverMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityApproverMock);
        when(shainEntityApproverMock.get("shainCd")).thenReturn(mock(Path.class));
        //Predicate predicateShainCdApproverMock = mock(Predicate.class);

        //when(criteriaBuilderMock.equal(shainEntityApproverMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdApproverMock);
        when(approvalFlowDetailsEntitiesApproverMock.get("shainNm")).thenReturn(mock(Path.class));
        Predicate predicateApproverMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(approvalFlowDetailsEntitiesApproverMock.get("shainNm"), "%" + filterRequest.getApprover() + "%")).thenReturn(predicateApproverMock);

        predicateFiltersMock.add(predicateApproverMock);
        //Predicate predicateApproverAndShainCdApproverMock = mock(Predicate.class);
        //when(criteriaBuilderMock.and(predicateApproverMock, predicateShainCdApproverMock)).thenReturn(predicateApproverAndShainCdApproverMock);
        //predicateFiltersMock.add(predicateApproverAndShainCdApproverMock);

        // 28
        Join mstRequestStatusesEntityMock = mock(Join.class);
        when(rootMock.join("mstRequestStatusesEntity", JoinType.INNER)).thenReturn(mstRequestStatusesEntityMock);
        Predicate predicateIsSentBackMock = mock(Predicate.class);
        when(rootMock.get("isSentBack")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(rootMock.get("isSentBack"), 1)).thenReturn(predicateIsSentBackMock);
        predicateFiltersMock.add(predicateIsSentBackMock);

        Predicate predicateRejectDefaultExpected = mock(Predicate.class);
        Predicate[] predicateFilterArrayMock = predicateFiltersMock.toArray(new Predicate[predicateFiltersMock.size()]);
        when(criteriaBuilderMock.and(predicateFilterArrayMock)).thenReturn(predicateRejectDefaultExpected);

        Specification<RequestsEntity> actual = filterRequestAll(shainCd, filterRequest);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(true);
    }

    @Test
    public void filterRequestListApplyOrApprove_With_Success() {
        String shainCd = "118602";

        Join approvalFlowDetailsEntitiesMock = mock(Join.class);
        Join shainEntityMock = mock(Join.class);

        when(rootMock.join("approvalFlowDetailsEntities", JoinType.INNER)).thenReturn(approvalFlowDetailsEntitiesMock);
        when(approvalFlowDetailsEntitiesMock.join("shainEntity", JoinType.INNER)).thenReturn(shainEntityMock);

        when(rootMock.get("stepNumber")).thenReturn(mock(Path.class));
        when(approvalFlowDetailsEntitiesMock.get("stepNumber")).thenReturn(mock(Path.class));

        Predicate predicateApprovalflowDetailMock = mock(Predicate.class);
        when(criteriaBuilderMock.equal(rootMock.get("stepNumber"), approvalFlowDetailsEntitiesMock.get("stepNumber"))).thenReturn(predicateApprovalflowDetailMock);

        Predicate predicateShainCdMock = mock(Predicate.class);
        when(shainEntityMock.get("shainCd")).thenReturn(mock(Path.class));
        when(criteriaBuilderMock.equal(shainEntityMock.get("shainCd"), shainCd)).thenReturn(predicateShainCdMock);

        Predicate predicateExpexted = mock(Predicate.class);
        when(criteriaBuilderMock.and(predicateApprovalflowDetailMock, predicateShainCdMock)).thenReturn(predicateExpexted);

        Specification<RequestsEntity> actual = requestListApplyOrApprove(shainCd);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(predicateExpexted);
    }
}
