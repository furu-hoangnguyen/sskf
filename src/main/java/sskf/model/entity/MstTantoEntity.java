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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_tanto", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstTantoEntity extends BaseTimeModel {

    @Id
    @Column(name = "tanto_cd")
    private String tantoCd;

    @Column(name = "tanto_status")
    private String tantoStatus;

    @Column(name = "batch_update_date")
    private LocalDateTime batchUpdateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "mstTantoEntity", fetch = FetchType.LAZY)
    private Set<MstStoreEntity> mstStoreEntities;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstTantoEntity that = (MstTantoEntity) o;
        return Objects.equals(tantoCd, that.tantoCd) &&
                Objects.equals(tantoStatus, that.tantoStatus) &&
                Objects.equals(batchUpdateDate, that.batchUpdateDate) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tantoCd, tantoStatus, batchUpdateDate, createdAt, updatedAt);
    }
}
