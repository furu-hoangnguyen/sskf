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
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "details_for_accounts_receivables", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class DetailsForAccountsReceivablesEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "type_of_item")
    private Byte typeOfItem;

    @Column(name = "hinmoku_cd")
    private String hinmokuCd;

    @Column(name = "hinmoku_rnm")
    private String hinmokuRnm;

    @Column(name = "yoryo")
    private String yoryo;

    @Column(name = "irisu")
    private Long irisu;

    @Column(name = "initial_irisu")
    private Long initialIrisu;

    @Column(name = "mf_yen")
    private BigDecimal mfYen;

    @Column(name = "initial_mf_yen")
    private BigDecimal initialMfYen;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "initial_commission")
    private BigDecimal initialCommission;

    @Column(name = "c_s_discount")
    private BigDecimal cSDiscount;

    @Column(name = "initial_c_s_discount")
    private BigDecimal initialCSDiscount;

    @Column(name = "denpyo_net")
    private BigDecimal denpyoNet;

    @Column(name = "initial_denpyo_net")
    private BigDecimal initialDenpyoNet;

    @Column(name = "accrued_unit_price")
    private BigDecimal accruedUnitPrice;

    @Column(name = "initial_accrued_unit_price")
    private BigDecimal initialAccruedUnitPrice;

    @Column(name = "final_take_unit_price")
    private BigDecimal finalTakeUnitPrice;

    @Column(name = "initial_final_take_unit_price")
    private BigDecimal initialFinalTakeUnitPrice;

    @Column(name = "hyojyun_yen")
    private BigDecimal hyojyunYen;

    @Column(name = "initial_hyojyun_yen")
    private BigDecimal initialHyojyunYen;

    @Column(name = "quantity_of_sold_items")
    private Long quantityOfSoldItems;

    @Column(name = "initial_quantity_of_sold_items")
    private Long initialQuantityOfSoldItems;

    @Column(name = "accrued_amount")
    private Long accruedAmount;

    @Column(name = "initial_accrued_amount")
    private Long initialAccruedAmount;

    @Column(name = "sales_amount")
    private Long salesAmount;

    @Column(name = "initial_sales_amount")
    private Long initialSalesAmount;

    @Column(name = "final_marginal_profit")
    private Long finalMarginalProfit;

    @Column(name = "initial_final_marginal_profit")
    private Long initialFinalMarginalProfit;

    @Column(name = "final_marginal_profit_ratio")
    private BigDecimal finalMarginalProfitRatio;

    @Column(name = "initial_final_marginal_profit_ratio")
    private BigDecimal initialFinalMarginalProfitRatio;

    @Column(name = "mishu_limit")
    private BigDecimal mishuLimit;

    @Column(name = "is_being_created")
    private Byte isBeingCreated;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_accounts_receivable_detail_cd")
    private RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailsForAccountsReceivablesEntity that = (DetailsForAccountsReceivablesEntity) o;
        return cd == that.cd &&
                typeOfItem == that.typeOfItem &&
                irisu == that.irisu &&
                initialIrisu == that.initialIrisu &&
                quantityOfSoldItems == that.quantityOfSoldItems &&
                initialQuantityOfSoldItems == that.initialQuantityOfSoldItems &&
                accruedAmount == that.accruedAmount &&
                initialAccruedAmount == that.initialAccruedAmount &&
                salesAmount == that.salesAmount &&
                initialSalesAmount == that.initialSalesAmount &&
                finalMarginalProfit == that.finalMarginalProfit &&
                initialFinalMarginalProfit == that.initialFinalMarginalProfit &&
                Objects.equals(hinmokuCd, that.hinmokuCd) &&
                Objects.equals(hinmokuRnm, that.hinmokuRnm) &&
                Objects.equals(yoryo, that.yoryo) &&
                Objects.equals(mfYen, that.mfYen) &&
                Objects.equals(initialMfYen, that.initialMfYen) &&
                Objects.equals(commission, that.commission) &&
                Objects.equals(initialCommission, that.initialCommission) &&
                Objects.equals(cSDiscount, that.cSDiscount) &&
                Objects.equals(initialCSDiscount, that.initialCSDiscount) &&
                Objects.equals(denpyoNet, that.denpyoNet) &&
                Objects.equals(initialDenpyoNet, that.initialDenpyoNet) &&
                Objects.equals(accruedUnitPrice, that.accruedUnitPrice) &&
                Objects.equals(initialAccruedUnitPrice, that.initialAccruedUnitPrice) &&
                Objects.equals(finalTakeUnitPrice, that.finalTakeUnitPrice) &&
                Objects.equals(initialFinalTakeUnitPrice, that.initialFinalTakeUnitPrice) &&
                Objects.equals(hyojyunYen, that.hyojyunYen) &&
                Objects.equals(initialHyojyunYen, that.initialHyojyunYen) &&
                Objects.equals(finalMarginalProfitRatio, that.finalMarginalProfitRatio) &&
                Objects.equals(initialFinalMarginalProfitRatio, that.initialFinalMarginalProfitRatio) &&
                Objects.equals(mishuLimit, that.mishuLimit) &&
                Objects.equals(isBeingCreated, that.isBeingCreated) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, typeOfItem, hinmokuCd, hinmokuRnm, yoryo, irisu, initialIrisu, mfYen, initialMfYen, commission, initialCommission, cSDiscount, initialCSDiscount, denpyoNet, initialDenpyoNet, accruedUnitPrice, initialAccruedUnitPrice, finalTakeUnitPrice, initialFinalTakeUnitPrice, hyojyunYen, initialHyojyunYen, quantityOfSoldItems, initialQuantityOfSoldItems, accruedAmount, initialAccruedAmount, salesAmount, initialSalesAmount, finalMarginalProfit, initialFinalMarginalProfit, finalMarginalProfitRatio, initialFinalMarginalProfitRatio, mishuLimit, isBeingCreated, createdAt, updatedAt);
    }
}
