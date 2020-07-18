package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class RequestsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "settlement_number")
    private String settlementNumber;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "is_sent_back")
    private Byte isSentBack;

    @Column(name = "mst_torihiki_cd")
    private String mstTorihikiCd;

    @Column(name = "torihiki_nm")
    private String torihikiNm;

    @Column(name = "billing_amount")
    private Long billingAmount;

    @Column(name = "initial_billing_amount")
    private Long initialBillingAmount;

    @Column(name = "billing_on")
    private Date billingOn;

    @Column(name = "payment_place")
    private String paymentPlace;

    @Column(name = "payment_method")
    private Byte paymentMethod;

    @Column(name = "payment_other_method")
    private String paymentOtherMethod;

    @Column(name = "scheduled_payment_on")
    private Date scheduledPaymentOn;

    @Column(name = "payment_on")
    private Date paymentOn;

    @Column(name = "payment_destination")
    private String paymentDestination;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_account_name")
    private String bankAccountName;

    @Column(name = "item_total_for_eight_percent")
    private Long itemTotalForEightPercent;

    @Column(name = "initial_item_total_for_eight_percent")
    private Long initialItemTotalForEightPercent;

    @Column(name = "item_total_for_ten_percent")
    private Long itemTotalForTenPercent;

    @Column(name = "initial_item_total_for_ten_percent")
    private Long initialItemTotalForTenPercent;

    @Column(name = "consumption_tax_total_for_eight_percent")
    private Long consumptionTaxTotalForEightPercent;

    @Column(name = "initial_consumption_tax_total_for_eight_percent")
    private Long initialConsumptionTaxTotalForEightPercent;

    @Column(name = "consumption_tax_total_for_ten_percent")
    private Long consumptionTaxTotalForTenPercent;

    @Column(name = "initial_consumption_tax_total_for_ten_percent")
    private Long initialConsumptionTaxTotalForTenPercent;

    @Column(name = "item_total")
    private Long itemTotal;

    @Column(name = "initial_item_total")
    private Long initialItemTotal;

    @Column(name = "consumption_tax_total")
    private Long consumptionTaxTotal;

    @Column(name = "initial_consumption_tax_total")
    private Long initialConsumptionTaxTotal;

    @Column(name = "total_for_eight_percent")
    private Long totalForEightPercent;

    @Column(name = "initial_total_for_eight_percent")
    private Long initialTotalForEightPercent;

    @Column(name = "total_for_ten_percent")
    private Long totalForTenPercent;

    @Column(name = "initial_total_for_ten_percent")
    private Long initialTotalForTenPercent;

    @Column(name = "total")
    private Long total;

    @Column(name = "initial_total")
    private Long initialTotal;

    @Column(name = "updated_status_at")
    private LocalDateTime updatedStatusAt;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "is_alerted_for_remind")
    private Byte isAlertedForRemind;

    @Column(name = "step_number")
    private Byte stepNumber;

    @Column(name = "updated_at_after_accounting_check")
    private LocalDateTime updatedAtAfterAccountingCheck;

    @Column(name = "started_edit_at")
    private LocalDateTime startedEditAt;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY)
    private Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY)
    private Set<OperationHistoriesEntity> operationHistoriesEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity")
    private Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY)
    private Set<RequestAttachmentsEntity> requestAttachmentsEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY)
    private Set<RequestExhibitionPromotionsEntity> requestExhibitionPromotionsEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY)
    private Set<RequestMannequinPromotionsEntity> requestMannequinPromotionsEntities;

    @JsonIgnore
    @OneToMany(mappedBy="requestsEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RequestNumbersEntity> requestNumbersEntities;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mst_request_type_cd")
    private MstRequestTypesEntity mstRequestTypesEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mst_approvalflow_cd")
    private MstApprovalFlowsEntity mstApprovalFlowsEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_cd")
    private MstRequestStatusesEntity mstRequestStatusesEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edit_shain_cd")
    private ShainEntity editShainEntity;

    public void setInitialValue() {
        this.initialBillingAmount = billingAmount;
        this.isDeleted = 0;
        this.stepNumber = 1;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestsEntity that = (RequestsEntity) o;
        return cd == that.cd &&
                billingAmount == that.billingAmount &&
                initialBillingAmount == that.initialBillingAmount &&
                paymentMethod == that.paymentMethod &&
                itemTotal == that.itemTotal &&
                initialItemTotal == that.initialItemTotal &&
                consumptionTaxTotal == that.consumptionTaxTotal &&
                initialConsumptionTaxTotal == that.initialConsumptionTaxTotal &&
                total == that.total &&
                initialTotal == that.initialTotal &&
                Objects.equals(settlementNumber, that.settlementNumber) &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(isSentBack, that.isSentBack) &&
                Objects.equals(mstTorihikiCd, that.mstTorihikiCd) &&
                Objects.equals(torihikiNm, that.torihikiNm) &&
                Objects.equals(billingOn, that.billingOn) &&
                Objects.equals(paymentPlace, that.paymentPlace) &&
                Objects.equals(paymentOtherMethod, that.paymentOtherMethod) &&
                Objects.equals(scheduledPaymentOn, that.scheduledPaymentOn) &&
                Objects.equals(paymentOn, that.paymentOn) &&
                Objects.equals(paymentDestination, that.paymentDestination) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(bankAccountNumber, that.bankAccountNumber) &&
                Objects.equals(bankAccountName, that.bankAccountName) &&
                Objects.equals(itemTotalForEightPercent, that.itemTotalForEightPercent) &&
                Objects.equals(initialItemTotalForEightPercent, that.initialItemTotalForEightPercent) &&
                Objects.equals(itemTotalForTenPercent, that.itemTotalForTenPercent) &&
                Objects.equals(initialItemTotalForTenPercent, that.initialItemTotalForTenPercent) &&
                Objects.equals(consumptionTaxTotalForEightPercent, that.consumptionTaxTotalForEightPercent) &&
                Objects.equals(initialConsumptionTaxTotalForEightPercent, that.initialConsumptionTaxTotalForEightPercent) &&
                Objects.equals(consumptionTaxTotalForTenPercent, that.consumptionTaxTotalForTenPercent) &&
                Objects.equals(initialConsumptionTaxTotalForTenPercent, that.initialConsumptionTaxTotalForTenPercent) &&
                Objects.equals(totalForEightPercent, that.totalForEightPercent) &&
                Objects.equals(initialTotalForEightPercent, that.initialTotalForEightPercent) &&
                Objects.equals(totalForTenPercent, that.totalForTenPercent) &&
                Objects.equals(initialTotalForTenPercent, that.initialTotalForTenPercent) &&
                Objects.equals(updatedStatusAt, that.updatedStatusAt) &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(isAlertedForRemind, that.isAlertedForRemind) &&
                Objects.equals(stepNumber, that.stepNumber) &&
                Objects.equals(updatedAtAfterAccountingCheck, that.updatedAtAfterAccountingCheck) &&
                Objects.equals(startedEditAt, that.startedEditAt) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, settlementNumber, isDeleted, isSentBack, mstTorihikiCd, torihikiNm, billingAmount, initialBillingAmount, billingOn, paymentPlace, paymentMethod, paymentOtherMethod, scheduledPaymentOn, paymentOn, paymentDestination, bankName, bankAccountNumber, bankAccountName, itemTotalForEightPercent, initialItemTotalForEightPercent, itemTotalForTenPercent, initialItemTotalForTenPercent, consumptionTaxTotalForEightPercent, initialConsumptionTaxTotalForEightPercent, consumptionTaxTotalForTenPercent, initialConsumptionTaxTotalForTenPercent, itemTotal, initialItemTotal, consumptionTaxTotal, initialConsumptionTaxTotal, totalForEightPercent, initialTotalForEightPercent, totalForTenPercent, initialTotalForTenPercent, total, initialTotal, updatedStatusAt, requestedAt, isAlertedForRemind, stepNumber, updatedAtAfterAccountingCheck, startedEditAt, createdAt, updatedAt);
    }
}
