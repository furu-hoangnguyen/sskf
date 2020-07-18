package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_accounts_receivable_details", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestAccountsReceivableDetailsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "store_g_cd")
    private String storeGCd;

    @Column(name = "store_g_nm")
    private String storeGNm;

    @Column(name = "shain_nm")
    private String shainNm;

    @Column(name = "bumon_nm")
    private String bumonNm;

    @Column(name = "is_checked")
    private Byte isChecked;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "sort_number")
    private Long sortNumber;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_accounts_receivable_cd")
    private RequestAccountsReceivablesEntity requestAccountsReceivablesEntity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bumon_cd")
    private MstBumonEntity mstBumonEntity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "requestAccountsReceivableDetailsEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DetailsForPromotionalExpensesEntity> detailsForPromotionalExpensesEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "requestAccountsReceivableDetailsEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DetailsForAccountsReceivablesEntity> detailsForAccountsReceivablesEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "requestAccountsReceivableDetailsEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CommentsForDetailsEntity> commentsForDetailsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAccountsReceivableDetailsEntity that = (RequestAccountsReceivableDetailsEntity) o;
        return cd == that.cd &&
                Objects.equals(itemNumber, that.itemNumber) &&
                Objects.equals(storeGCd, that.storeGCd) &&
                Objects.equals(storeGNm, that.storeGNm) &&
                Objects.equals(shainNm, that.shainNm) &&
                Objects.equals(bumonNm, that.bumonNm) &&
                Objects.equals(isChecked, that.isChecked) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, itemNumber, storeGCd, storeGNm, shainNm, bumonNm, isChecked, createdAt, updatedAt);
    }
}
