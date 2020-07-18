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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mst_approvalflow_details", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstApprovalFlowDetailsEntity extends BaseTimeModel {

    @Id
    @Column(name = "cd")
    private String cd;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "step_number")
    private Byte stepNumber;

    @Column(name = "is_deputy")
    private Byte isDeputy;

    @ManyToOne
    @JoinColumn(name = "approvalflow_cd")
    private MstApprovalFlowsEntity mstApprovalFlowsEntity;

    @ManyToOne
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "yakusyoku_cd")
    private MstYakushokuEntity mstYakushokuEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstApprovalFlowDetailsEntity that = (MstApprovalFlowDetailsEntity) o;
        return stepNumber == that.stepNumber &&
                Objects.equals(cd, that.cd) &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(isDeputy, that.isDeputy) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, isDeleted, stepNumber, isDeputy, createdAt, updatedAt);
    }
}
