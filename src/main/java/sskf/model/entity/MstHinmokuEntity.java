package sskf.model.entity;

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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "mst_hinmoku", schema = "ssk_accounts_receivable")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class MstHinmokuEntity extends BaseTimeModel {
    @Id
    @Column(name = "hinmoku_cd")
    private String hinmokuCd;

    @Column(name = "hinmoku_cd_nk")
    private String hinmokuCdNk;

    @Column(name = "brand_kbn")
    private String brandKbn;

    @Column(name = "han_keitai_kbn")
    private String hanKeitaiKbn;

    @Column(name = "hinmoku_kbn")
    private String hinmokuKbn;

    @Column(name = "jigyo_cd")
    private String jigyoCd;

    @Column(name = "hinmoku_knm")
    private String hinmokuKnm;

    @Column(name = "hinmoku_rnm")
    private String hinmokuRnm;

    @Column(name = "hinmoku_knm_for_search")
    private String hinmokuKnmForSearch;

    @Column(name = "hinmoku_rnm_for_search")
    private String hinmokuRnmForSearch;

    @Column(name = "kikaku")
    private Integer kikaku;

    @Column(name = "nisugata")
    private String nisugata;

    @Column(name = "nisugata_for_search")
    private String nisugataForSearch;

    @Column(name = "irisu")
    private Integer irisu;

    @Column(name = "yoryo_tani")
    private String yoryoTani;

    @Column(name = "batch_update_date_hinmoku")
    private LocalDateTime batchUpdateDateHinmoku;

    @Column(name = "update_date_aw")
    private LocalDateTime updateDateAw;

    @Column(name = "update_date_mb")
    private LocalDateTime updateDateMb;

    @Column(name = "mf_yen")
    private BigDecimal mfYen;

    @Column(name = "hyojyun_yen")
    private BigDecimal hyojyunYen;

    @Column(name = "mishu_limit")
    private BigDecimal mishuLimit;

    @Column(name = "category_hin_kbn")
    private String categoryHinKbn;

    @Column(name = "category_hinsub_kbn")
    private String categoryHinsubKbn;

    @Column(name = "category_series_kbn")
    private String categorySeriesKbn;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstHinmokuEntity that = (MstHinmokuEntity) o;
        return Objects.equals(hinmokuCd, that.hinmokuCd) &&
                Objects.equals(hinmokuCdNk, that.hinmokuCdNk) &&
                Objects.equals(brandKbn, that.brandKbn) &&
                Objects.equals(hanKeitaiKbn, that.hanKeitaiKbn) &&
                Objects.equals(hinmokuKbn, that.hinmokuKbn) &&
                Objects.equals(jigyoCd, that.jigyoCd) &&
                Objects.equals(hinmokuKnm, that.hinmokuKnm) &&
                Objects.equals(hinmokuRnm, that.hinmokuRnm) &&
                Objects.equals(hinmokuKnmForSearch, that.hinmokuKnmForSearch) &&
                Objects.equals(hinmokuRnmForSearch, that.hinmokuRnmForSearch) &&
                Objects.equals(kikaku, that.kikaku) &&
                Objects.equals(nisugata, that.nisugata) &&
                Objects.equals(nisugataForSearch, that.nisugataForSearch) &&
                Objects.equals(irisu, that.irisu) &&
                Objects.equals(yoryoTani, that.yoryoTani) &&
                Objects.equals(batchUpdateDateHinmoku, that.batchUpdateDateHinmoku) &&
                Objects.equals(updateDateAw, that.updateDateAw) &&
                Objects.equals(updateDateMb, that.updateDateMb) &&
                Objects.equals(mfYen, that.mfYen) &&
                Objects.equals(hyojyunYen, that.hyojyunYen) &&
                Objects.equals(mishuLimit, that.mishuLimit) &&
                Objects.equals(categoryHinKbn, that.categoryHinKbn) &&
                Objects.equals(categoryHinsubKbn, that.categoryHinsubKbn) &&
                Objects.equals(categorySeriesKbn, that.categorySeriesKbn) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hinmokuCd, hinmokuCdNk, brandKbn, hanKeitaiKbn, hinmokuKbn, jigyoCd, hinmokuKnm, hinmokuRnm, hinmokuKnmForSearch, hinmokuRnmForSearch, kikaku, nisugata, nisugataForSearch, irisu, yoryoTani, batchUpdateDateHinmoku, updateDateAw, updateDateMb, mfYen, hyojyunYen, mishuLimit, categoryHinKbn, categoryHinsubKbn, categorySeriesKbn, createdAt, updatedAt);
    }
}
