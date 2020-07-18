package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class DetailsForPromotionalExpensesRequest extends AccountReceivablesDetailRequest {

    private Long cd;

    @NotEmpty(message = "typeOfInput is required!")
    private String typeOfInput;

    @NotEmpty(message = "typeOfPromotionalExpenses is required!")
    private String typeOfPromotionalExpenses;

    private String classification;

    private String brandClassification;

    private String categoryName;

    private String hinmokuCd;

    private String hinmokuRnm;

    private String nisugata;

    @NotNull(message = "accruedAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "accruedAmount amount no more than 12 characters!")
    private Long accruedAmount;

    @NotNull(message = "initialAccruedAmount is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialAccruedAmount amount no more than 12 characters!")
    private Long initialAccruedAmount;

}
