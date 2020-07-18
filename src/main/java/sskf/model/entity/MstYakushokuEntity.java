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
@Table(name = "mst_yakushoku", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstYakushokuEntity extends BaseTimeModel {

    @Id
    @Column(name = "cd")
    private String cd;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "mstYakushokuEntity", fetch = FetchType.LAZY)
    private Set<MstApprovalFlowDetailsEntity> mstApprovalFlowDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "mstYakushokuEntity", fetch = FetchType.LAZY)
    private Set<MstRelYakushokuShainEntity> mstRelYakushokuShainEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstYakushokuEntity that = (MstYakushokuEntity) o;
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
