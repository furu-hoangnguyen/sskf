package sskf.model.entity;

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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_torihiki", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstTorihikiEntity extends BaseTimeModel {

    @Id
    @Column(name = "torihiki_cd")
    private String torihikiCd;

    @Column(name = "torihiki_cd_nk")
    private String torihikiCdNk;

    @Column(name = "torihiki1_nm")
    private String torihiki1Nm;

    @Column(name = "torihiki2_nm")
    private String torihiki2Nm;

    @Column(name = "torihiki_rnm")
    private String torihikiRnm;

    @Column(name = "torihiki_rnm_for_search")
    private String torihikiRnmForSearch;

    @Column(name = "torihiki_rknm")
    private String torihikiRknm;

    @Column(name = "torihiki_rknm_for_search")
    private String torihikiRknmForSearch;

    @Column(name = "seikyu_flg")
    private String seikyuFlg;

    @Column(name = "choai_kori_flg")
    private String choaiKoriFlg;

    @Column(name = "torihiki_status")
    private String torihikiStatus;

    @Column(name = "batch_update_date")
    private LocalDateTime batchUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstTorihikiEntity that = (MstTorihikiEntity) o;
        return Objects.equals(torihikiCd, that.torihikiCd) &&
                Objects.equals(torihikiCdNk, that.torihikiCdNk) &&
                Objects.equals(torihiki1Nm, that.torihiki1Nm) &&
                Objects.equals(torihiki2Nm, that.torihiki2Nm) &&
                Objects.equals(torihikiRnm, that.torihikiRnm) &&
                Objects.equals(torihikiRnmForSearch, that.torihikiRnmForSearch) &&
                Objects.equals(torihikiRknm, that.torihikiRknm) &&
                Objects.equals(torihikiRknmForSearch, that.torihikiRknmForSearch) &&
                Objects.equals(seikyuFlg, that.seikyuFlg) &&
                Objects.equals(choaiKoriFlg, that.choaiKoriFlg) &&
                Objects.equals(torihikiStatus, that.torihikiStatus) &&
                Objects.equals(batchUpdateDate, that.batchUpdateDate) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(torihikiCd, torihikiCdNk, torihiki1Nm, torihiki2Nm, torihikiRnm, torihikiRnmForSearch, torihikiRknm, torihikiRknmForSearch, seikyuFlg, choaiKoriFlg, torihikiStatus, batchUpdateDate, createdAt, updatedAt);
    }
}
