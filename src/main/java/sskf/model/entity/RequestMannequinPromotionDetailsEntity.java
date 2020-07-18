package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_mannequin_promotion_details", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestMannequinPromotionDetailsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "hinmoku_cd")
    private String hinmokuCd;

    @Column(name = "hinmoku_rnm")
    private String hinmokuRnm;

    @Column(name = "nisugata")
    private String nisugata;

    @Column(name = "irisu")
    private Long irisu;

    @Column(name = "initial_irisu")
    private Long initialIrisu;

    @Column(name = "mf_yen")
    private BigDecimal mfYen;

    @Column(name = "initial_mf_yen")
    private BigDecimal initialMfYen;

    @Column(name = "hyojyun_yen")
    private BigDecimal hyojyunYen;

    @Column(name = "initial_hyojyun_yen")
    private BigDecimal initialHyojyunYen;

    @Column(name = "numbers_of_cases")
    private Long numbersOfCases;

    @Column(name = "initial_numbers_of_cases")
    private Long initialNumbersOfCases;

    @Column(name = "sales_of_seihan")
    private Long salesOfSeihan;

    @Column(name = "initial_sales_of_seihan")
    private Long initialSalesOfSeihan;

    @Column(name = "standard_marginal_profit")
    private Long standardMarginalProfit;

    @Column(name = "initial_standard_marginal_profit")
    private Long initialStandardMarginalProfit;

    @Column(name = "accrued_amount")
    private Long accruedAmount;

    @Column(name = "initial_accrued_amount")
    private Long initialAccruedAmount;

    @Column(name = "costs_of_mannequin")
    private Long costsOfMannequin;

    @Column(name = "initial_costs_of_mannequin")
    private Long initialCostsOfMannequin;

    @Column(name = "final_marginal_profit")
    private Long finalMarginalProfit;

    @Column(name = "initial_final_marginal_profit")
    private Long initialFinalMarginalProfit;

    @Column(name = "sort_number")
    private Long sortNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_mannequin_promotion_cd")
    private RequestMannequinPromotionsEntity requestMannequinPromotionsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMannequinPromotionDetailsEntity that = (RequestMannequinPromotionDetailsEntity) o;
        return cd == that.cd &&
                irisu == that.irisu &&
                initialIrisu == that.initialIrisu &&
                numbersOfCases == that.numbersOfCases &&
                initialNumbersOfCases == that.initialNumbersOfCases &&
                salesOfSeihan == that.salesOfSeihan &&
                initialSalesOfSeihan == that.initialSalesOfSeihan &&
                standardMarginalProfit == that.standardMarginalProfit &&
                initialStandardMarginalProfit == that.initialStandardMarginalProfit &&
                accruedAmount == that.accruedAmount &&
                initialAccruedAmount == that.initialAccruedAmount &&
                costsOfMannequin == that.costsOfMannequin &&
                initialCostsOfMannequin == that.initialCostsOfMannequin &&
                finalMarginalProfit == that.finalMarginalProfit &&
                initialFinalMarginalProfit == that.initialFinalMarginalProfit &&
                Objects.equals(hinmokuCd, that.hinmokuCd) &&
                Objects.equals(hinmokuRnm, that.hinmokuRnm) &&
                Objects.equals(nisugata, that.nisugata) &&
                Objects.equals(mfYen, that.mfYen) &&
                Objects.equals(initialMfYen, that.initialMfYen) &&
                Objects.equals(hyojyunYen, that.hyojyunYen) &&
                Objects.equals(initialHyojyunYen, that.initialHyojyunYen) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, hinmokuCd, hinmokuRnm, nisugata, irisu, initialIrisu, mfYen, initialMfYen, hyojyunYen, initialHyojyunYen, numbersOfCases, initialNumbersOfCases, salesOfSeihan, initialSalesOfSeihan, standardMarginalProfit, initialStandardMarginalProfit, accruedAmount, initialAccruedAmount, costsOfMannequin, initialCostsOfMannequin, finalMarginalProfit, initialFinalMarginalProfit, createdAt, updatedAt);
    }
}
