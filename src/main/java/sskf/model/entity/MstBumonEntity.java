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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mst_bumon", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstBumonEntity extends BaseTimeModel {

    @Id
    @Column(name = "bumon_cd")
    private String bumonCd;

    @Column(name = "bumon_cd_nk")
    private String bumonCdNk;

    @Column(name = "bumon_nm")
    private String bumonNm;

    @Column(name = "bumon_knm")
    private String bumonKnm;

    @Column(name = "bumon_rnm")
    private String bumonRnm;

    @Column(name = "batch_update_date")
    private LocalDateTime batchUpdateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<MstApprovalFlowsEntity> mstApprovalFlowsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<MstApprovalFlowDetailsEntity> mstApprovalFlowDetailsEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<MstTantoEntity> mstTantoEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "mstBumonEntity", fetch = FetchType.LAZY)
    private Set<ShainEntity> shainEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstBumonEntity that = (MstBumonEntity) o;
        return Objects.equals(bumonCd, that.bumonCd) &&
                Objects.equals(bumonCdNk, that.bumonCdNk) &&
                Objects.equals(bumonNm, that.bumonNm) &&
                Objects.equals(bumonKnm, that.bumonKnm) &&
                Objects.equals(bumonRnm, that.bumonRnm) &&
                Objects.equals(batchUpdateDate, that.batchUpdateDate) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bumonCd, bumonCdNk, bumonNm, bumonKnm, bumonRnm, batchUpdateDate, createdAt, updatedAt);
    }
}
