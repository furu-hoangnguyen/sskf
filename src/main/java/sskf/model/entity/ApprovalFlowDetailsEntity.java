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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "approvalflow_details", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class ApprovalFlowDetailsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @Column(name = "step_number")
    private Byte stepNumber;

    @ManyToOne
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @Column(name = "bumon_nm")
    private String bumonNm;

    @ManyToOne
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

    @Column(name = "shain_nm")
    private String shainNm;

    @Column(name = "is_deputy")
    private Boolean isDeputy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApprovalFlowDetailsEntity that = (ApprovalFlowDetailsEntity) o;
        return cd == that.cd &&
                stepNumber == that.stepNumber &&
                Objects.equals(bumonNm, that.bumonNm) &&
                Objects.equals(shainNm, that.shainNm) &&
                Objects.equals(isDeputy, that.isDeputy) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, stepNumber, bumonNm, shainNm, isDeputy, createdAt, updatedAt);
    }
}
