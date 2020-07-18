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
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_store", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstStoreEntity extends BaseTimeModel {

    @Id
    @Column(name = "store_cd")
    private String storeCd;

    @Column(name = "store_g_cd")
    private String storeGCd;

    @Column(name = "store_g_nm")
    private String storeGNm;

    @Column(name = "store_g_nm_for_search")
    private String storeGNmForSearch;

    @Column(name = "update_date_aw")
    private LocalDateTime updateDateAw;

    @Column(name = "update_date_mb")
    private LocalDateTime updateDateMb;

    @Column(name = "delete_flg")
    private Byte deleteFlg;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_g_tanto_cd")
    private MstTantoEntity mstTantoEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstStoreEntity that = (MstStoreEntity) o;
        return Objects.equals(storeCd, that.storeCd) &&
                Objects.equals(storeGCd, that.storeGCd) &&
                Objects.equals(storeGNm, that.storeGNm) &&
                Objects.equals(storeGNmForSearch, that.storeGNmForSearch) &&
                Objects.equals(updateDateAw, that.updateDateAw) &&
                Objects.equals(updateDateMb, that.updateDateMb) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCd, storeGCd, storeGNm, storeGNmForSearch, updateDateAw, updateDateMb, createdAt, updatedAt);
    }
}
