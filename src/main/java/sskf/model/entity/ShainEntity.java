package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shain", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class ShainEntity extends BaseTimeModel {

    @Id
    @Column(name = "shain_cd")
    private String shainCd;

    @Column(name = "shain_cd_nk")
    private String shainCdNk;

    @Column(name = "shain_nm")
    private String shainNm;

    @Column(name = "password")
    private String password;

    @Column(name = "batch_update_date")
    private Timestamp batchUpdateDate;

    @Column(name = "is_alerted_for_application")
    private Byte isAlertedForApplication;

    @Column(name = "is_alerted_for_modification_request")
    private Byte isAlertedForModificationRequest;

    @Column(name = "is_alerted_for_rejection")
    private Byte isAlertedForRejection;

    @Column(name = "is_alerted_for_approval")
    private Byte isAlertedForApproval;

    @Column(name = "is_alerted_for_sending_request_back")
    private Byte isAlertedForSendingRequestBack;

    @Column(name = "is_alerted_for_confirming_send_request_back")
    private Byte isAlertedForConfirmingSendRequestBack;

    @Column(name = "is_alerted_for_input_payment_date")
    private Byte isAlertedForInputPaymentDate;

    @Column(name = "is_alerted_for_confirming_settlement")
    private Byte isAlertedForConfirmingSettlement;

    @Column(name = "is_alerted_for_application_deputy")
    private Byte isAlertedForApplicationDeputy;

    @Column(name = "is_alerted_for_approval_deputy")
    private Byte isAlertedForApprovalDeputy;

    @Column(name = "is_alerted_for_changing_charge")
    private Byte isAlertedForChangingCharge;

    @Column(name = "is_alerted_for_being_created")
    private Byte isAlertedForBeingCreated;

    @Column(name = "is_alerted_for_waiting_confirmation")
    private Byte isAlertedForWaitingConfirmation;

    @Column(name = "is_alerted_for_waiting_application")
    private Byte isAlertedForWaitingApplication;

    @Column(name = "is_alerted_for_waiting_approval")
    private Byte isAlertedForWaitingApproval;

    @Column(name = "is_alerted_for_waiting_application_on_sending_back")
    private Byte isAlertedForWaitingApplicationOnSendingBack;

    @Column(name = "is_alerted_for_waiting_approval_on_sending_back")
    private Byte isAlertedForWaitingApprovalOnSendingBack;

    @Column(name = "is_alerted_for_waiting_confirming_settlement")
    private Byte isAlertedForWaitingConfirmingSettlement;

    @Column(name = "is_alerted_for_updating_database")
    private Byte isAlertedForUpdatingDatabase;

    @ManyToOne
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<OperationHistoriesEntity> operationHistoriesEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<CommentsForDetailsEntity> commentsForDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<MstTantoEntity> mstTantoEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "shainEntity", fetch = FetchType.LAZY)
    private Set<MstRelYakushokuShainEntity> mstRelYakushokuShainEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "editShainEntity", fetch = FetchType.LAZY)
    private Set<RequestsEntity> requestsEntities;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "shain_cd")
    private ShainAddressEntity shainAddressEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShainEntity that = (ShainEntity) o;
        return Objects.equals(shainCd, that.shainCd) &&
                Objects.equals(shainCdNk, that.shainCdNk) &&
                Objects.equals(shainNm, that.shainNm) &&
                Objects.equals(password, that.password) &&
                Objects.equals(batchUpdateDate, that.batchUpdateDate) &&
                Objects.equals(isAlertedForApplication, that.isAlertedForApplication) &&
                Objects.equals(isAlertedForModificationRequest, that.isAlertedForModificationRequest) &&
                Objects.equals(isAlertedForRejection, that.isAlertedForRejection) &&
                Objects.equals(isAlertedForApproval, that.isAlertedForApproval) &&
                Objects.equals(isAlertedForSendingRequestBack, that.isAlertedForSendingRequestBack) &&
                Objects.equals(isAlertedForConfirmingSendRequestBack, that.isAlertedForConfirmingSendRequestBack) &&
                Objects.equals(isAlertedForInputPaymentDate, that.isAlertedForInputPaymentDate) &&
                Objects.equals(isAlertedForConfirmingSettlement, that.isAlertedForConfirmingSettlement) &&
                Objects.equals(isAlertedForApplicationDeputy, that.isAlertedForApplicationDeputy) &&
                Objects.equals(isAlertedForApprovalDeputy, that.isAlertedForApprovalDeputy) &&
                Objects.equals(isAlertedForChangingCharge, that.isAlertedForChangingCharge) &&
                Objects.equals(isAlertedForBeingCreated, that.isAlertedForBeingCreated) &&
                Objects.equals(isAlertedForWaitingConfirmation, that.isAlertedForWaitingConfirmation) &&
                Objects.equals(isAlertedForWaitingApplication, that.isAlertedForWaitingApplication) &&
                Objects.equals(isAlertedForWaitingApproval, that.isAlertedForWaitingApproval) &&
                Objects.equals(isAlertedForWaitingApplicationOnSendingBack, that.isAlertedForWaitingApplicationOnSendingBack) &&
                Objects.equals(isAlertedForWaitingApprovalOnSendingBack, that.isAlertedForWaitingApprovalOnSendingBack) &&
                Objects.equals(isAlertedForWaitingConfirmingSettlement, that.isAlertedForWaitingConfirmingSettlement) &&
                Objects.equals(isAlertedForUpdatingDatabase, that.isAlertedForUpdatingDatabase) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shainCd, shainCdNk, shainNm, password, batchUpdateDate, isAlertedForApplication, isAlertedForModificationRequest, isAlertedForRejection, isAlertedForApproval, isAlertedForSendingRequestBack, isAlertedForConfirmingSendRequestBack, isAlertedForInputPaymentDate, isAlertedForConfirmingSettlement, isAlertedForApplicationDeputy, isAlertedForApprovalDeputy, isAlertedForChangingCharge, isAlertedForBeingCreated, isAlertedForWaitingConfirmation, isAlertedForWaitingApplication, isAlertedForWaitingApproval, isAlertedForWaitingApplicationOnSendingBack, isAlertedForWaitingApprovalOnSendingBack, isAlertedForWaitingConfirmingSettlement, isAlertedForUpdatingDatabase, createdAt, updatedAt);
    }
}
