package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_systems", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstSystemsEntity extends BaseTimeModel {

    @Id
    @Column(name = "cd")
    private String cd;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "mstRelApprovalflowsSystemsEntityPK.mstSystemsEntity", fetch = FetchType.LAZY)
    private Set<MstRelApprovalFlowsSystemsEntity> mstRelApprovalFlowsSystemsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstSystemsEntity that = (MstSystemsEntity) o;
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
