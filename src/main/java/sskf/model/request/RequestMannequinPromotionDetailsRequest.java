package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class RequestMannequinPromotionDetailsRequest {

    private Long cd;

    @NotEmpty(message = "hinmokuCd is required!")
    private String hinmokuCd;

    @NotEmpty(message = "hinmokuRnm is required!")
    private String hinmokuRnm;

    @NotEmpty(message = "nisugata is required!")
    private String nisugata;

    @NotNull(message = "irisu is required!")
    private Long irisu;

    private Long initialIrisu;

    @NotNull(message = "mfYen is required!")
    private BigDecimal mfYen;

    private BigDecimal initialMfYen;

    @NotNull(message = "hyojyunYen is required!")
    private BigDecimal hyojyunYen;

    private BigDecimal initialHyojyunYen;

    @NotNull(message = "numbersOfCases is required!")
    private Long numbersOfCases;

    private Long initialNumbersOfCases;

    @NotNull(message = "salesOfSeihan is required!")
    private Long salesOfSeihan;

    private Long initialSalesOfSeihan;

    @NotNull(message = "standardMarginalProfit is required!")
    private Long standardMarginalProfit;

    private Long initialStandardMarginalProfit;

    @NotNull(message = "accruedAmount is required!")
    private Long accruedAmount;

    private Long initialAccruedAmount;

    @NotNull(message = "costsOfMannequin is required!")
    private Long costsOfMannequin;

    private Long initialCostsOfMannequin;

    @NotNull(message = "finalMarginalProfit is required!")
    private Long finalMarginalProfit;

    private Long initialFinalMarginalProfit;

    @NotNull(message = "sortNumber is required!")
    private Long sortNumber;

}
