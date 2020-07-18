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
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mst_approvalflows", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstApprovalFlowsEntity extends BaseTimeModel {

    @Id
    @Column(name = "cd")
    private String cd;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @JsonIgnore
    @OneToMany(mappedBy="mstApprovalFlowsEntity", fetch = FetchType.LAZY)
    private Set<MstApprovalFlowDetailsEntity> mstApprovalFlowDetailsEntity;

    @JsonIgnore
    @OneToMany(mappedBy="mstApprovalFlowsEntity", fetch = FetchType.LAZY)
    private Set<RequestsEntity> requestsEntity;

    @JsonIgnore
    @OneToMany(mappedBy="mstRelApprovalflowsSystemsEntityPK.mstApprovalFlowsEntity", fetch = FetchType.LAZY)
    private Set<MstRelApprovalFlowsSystemsEntity> mstRelApprovalFlowsSystemsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstApprovalFlowsEntity that = (MstApprovalFlowsEntity) o;
        return Objects.equals(cd, that.cd) &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(name, that.name) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, isDeleted, name, createdAt, updatedAt);
    }
}
