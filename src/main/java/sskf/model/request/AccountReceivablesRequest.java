package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AccountReceivablesRequest {

    private Long cd;

    @NotNull(message = "targetOn is required!")
    private Date targetOn;

    @NotEmpty(message = "purpose is required!")
    private String purpose;

    @Size(max = 36, message = "purposeOfOthers max 36 character!")
    private String purposeOfOthers;

    @NotEmpty(message = "commissionType is required!")
    private String commissionType;

    @Size(max = 1000, message = "remarks max 1000 character!")
    private String remarks;

    @Valid
    private RequestModel requestModel;

    private List<Long> accountReceivableDetailIsDeleted;

    @Valid
    private List<DetailsForAccountsReceivablesRequest> detailsForAccountsReceivablesRequestList;

    @Valid
    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax8PercentList;

    @Valid
    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax10PercentList;

    @Valid
    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestLogisticFeesList;
}
