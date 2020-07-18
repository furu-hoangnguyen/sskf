package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class DetailsForAccountsReceivablesRequest extends AccountReceivablesDetailRequest{

    private Long cd;

    @NotEmpty(message = "typeOfItem is required!")
    private String typeOfItem;

    @NotEmpty(message = "hinmokuCd is required!")
    private String hinmokuCd;

    private String hinmokuRnm;

    @NotEmpty(message = "yoryo is required!")
    private String yoryo;

    @Range(min= 1L, max = 999999999999L, message = "irisu min > 0 and max < 1000.000.000.000!")
    private Long irisu;

    @NotNull(message = "initialIrisu is required!")
    @Range(min= 1L, max = 999999999999L, message = "initialIrisu min > 0 and max < 1000.000.000.000!")
    private Long initialIrisu;

    @Range(max = 1000000L, message = "mfYen min > 0 and max < 1.000.000!")
    @NotNull(message = "mfYen is required!")
    private BigDecimal mfYen;

    @NotNull(message = "initialMfYen is required!")
    private BigDecimal initialMfYen;

    private BigDecimal commission;

    private BigDecimal initialCommission;

    private BigDecimal mishuLimit;

    @Range(max = 10000000000L, message = "cSDiscount min > 0 and max < 10.000.000.000!")
    private BigDecimal cSDiscount;

    @Range(max = 10000000000L, message = "initialCSDiscount min > 0 and max < 10.000.000.000!")
    private BigDecimal initialCSDiscount;

    @NotNull(message = "denpyoNet is required!")
    @Range(min= -10000000000L, max = 10000000000L, message = "denpyoNet min > -10.000.000.000 and max < 10.000.000.000!")
    private BigDecimal denpyoNet;

    @NotNull(message = "initialDenpyoNet is required!")
    @Range(min= -10000000000L, max = 10000000000L, message = "initialDenpyoNet min > -10.000.000.000 and max < 10.000.000.000!")
    private BigDecimal initialDenpyoNet;

    @NotNull(message = "accruedUnitPrice is required!")
    @Range(max = 10000000000L, message = "accruedUnitPrice min > 0 and max < 10.000.000.000!")
    private BigDecimal accruedUnitPrice;

    @NotNull(message = "initialAccruedUnitPrice is required!")
    @Range(max = 10000000000L, message = "initialAccruedUnitPrice min > 0 and max < 10.000.000.000!")
    private BigDecimal initialAccruedUnitPrice;

    @NotNull(message = "finalTakeUnitPrice is required!")
    @Range(min= -10000000000L, max = 10000000000L, message = "finalTakeUnitPrice min > -10.000.000.000 and max < 10.000.000.000!")
    private BigDecimal finalTakeUnitPrice;

    @NotNull(message = "initialFinalTakeUnitPrice is required!")
    @Range(min= -10000000000L, max = 10000000000L, message = "initialFinalTakeUnitPrice min > -10.000.000.000 and max < 10.000.000.000!")
    private BigDecimal initialFinalTakeUnitPrice;

    @NotNull(message = "hyojyunYen is required!")
    @Range(max = 1000000L, message = "hyojyunYen min > 0 and max < 1.000.000!")
    private BigDecimal hyojyunYen;

    @NotNull(message = "initialHyojyunYen is required!")
    @Range(max = 1000000L, message = "initialHyojyunYen min > 0 and max < 1.000.000!")
    private BigDecimal initialHyojyunYen;

    @NotNull(message = "quantityOfSoldItems is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "quantityOfSoldItems min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long quantityOfSoldItems;

    @NotNull(message = "initialQuantityOfSoldItems is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "quantityOfSoldItems min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialQuantityOfSoldItems;

    @NotNull(message = "accruedAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "accruedAmount min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long accruedAmount;

    @NotNull(message = "initialAccruedAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialAccruedAmount min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialAccruedAmount;

    @NotNull(message = "salesAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "salesAmount min > -1.000.000.000.000 and max < 1.000.000.000.000!")
    private Long salesAmount;

    @NotNull(message = "initialSalesAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialSalesAmount min > -1.000.000.000.000 and max < 1.000.000.000.000!")
    private Long initialSalesAmount;

    @NotNull(message = "finalMarginalProfit is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "finalMarginalProfitmin min > -1.000.000.000.000 and max < 1.000.000.000.000!")
    private Long finalMarginalProfit;

    @NotNull(message = "finalMarginalProfit is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialFinalMarginalProfit min > -1.000.000.000.000 and max < 1.000.000.000.000!")
    private Long initialFinalMarginalProfit;

    @NotNull(message = "finalMarginalProfitRatio is required!")
    private BigDecimal finalMarginalProfitRatio;

    @NotNull(message = "initialFinalMarginalProfitRatio is required!")
    private BigDecimal initialFinalMarginalProfitRatio;

}
