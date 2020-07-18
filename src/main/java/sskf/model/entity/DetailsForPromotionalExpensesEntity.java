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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "details_for_promotional_expenses", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class DetailsForPromotionalExpensesEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "type_of_promotional_expenses")
    private Byte typeOfPromotionalExpenses;

    @Column(name = "type_of_input")
    private Byte typeOfInput;

    @Column(name = "classification")
    private Byte classification;

    @Column(name = "brand_classification")
    private Byte brandClassification;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "hinmoku_cd")
    private String hinmokuCd;

    @Column(name = "hinmoku_rnm")
    private String hinmokuRnm;

    @Column(name = "nisugata")
    private String nisugata;

    @Column(name = "accrued_amount")
    private Long accruedAmount;

    @Column(name = "initial_accrued_amount")
    private Long initialAccruedAmount;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_accounts_receivable_detail_cd")
    private RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailsForPromotionalExpensesEntity that = (DetailsForPromotionalExpensesEntity) o;
        return cd == that.cd &&
                typeOfPromotionalExpenses == that.typeOfPromotionalExpenses &&
                typeOfInput == that.typeOfInput &&
                accruedAmount == that.accruedAmount &&
                initialAccruedAmount == that.initialAccruedAmount &&
                Objects.equals(classification, that.classification) &&
                Objects.equals(brandClassification, that.brandClassification) &&
                Objects.equals(categoryName, that.categoryName) &&
                Objects.equals(hinmokuCd, that.hinmokuCd) &&
                Objects.equals(hinmokuRnm, that.hinmokuRnm) &&
                Objects.equals(nisugata, that.nisugata) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, typeOfPromotionalExpenses, typeOfInput, classification, brandClassification, categoryName, hinmokuCd, hinmokuRnm, nisugata, accruedAmount, initialAccruedAmount, createdAt, updatedAt);
    }
}
