package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import sskf.model.FileModel;
import sskf.model.response.MstApprovalFlowsResponse;
import sskf.model.response.MstRequestStatusesResponse;
import sskf.model.response.RequestNumberResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RequestModel {

    private Long cd;

    private String mstTorihikiCd;

    @NotEmpty(message = "torihikiNm is required!")
    @Size(max = 80, message = "torihikiNm max 80 character!")
    private String torihikiNm;

    private Byte isSentBack;

    @Range(min = 1, max = 999999999999L, message = "Billing amount has must min = 1 and max = 999.999.999.999")
    private Long billingAmount;

    @NotNull(message = "billingOn is required!")
    private Date billingOn;

    @NotEmpty(message = "paymentPlace is required!")
    private String paymentPlace;

    @NotEmpty(message = "Payment method require!")
    private String paymentMethod;

    @Size(max = 200, message = "paymentOtherMethod max 200 character!")
    private String paymentOtherMethod;

    @NotNull(message = "scheduledPaymentOn is required!")
    private Date scheduledPaymentOn;

    private Date paymentOn;

    @NotEmpty(message = "paymentDestination is required!")
    @Size(max = 40, message = "paymentDestination max 40 character!")
    private String paymentDestination;

    @NotEmpty(message = "bankName is required!")
    @Size(max = 16, message = "bankName max 16 character!")
    private String bankName;

    @NotEmpty(message = "bankAccountNumber is required!")
    @Size(max = 10, message = "bankAccountNumber max 10 character!")
    private String bankAccountNumber;

    @NotEmpty(message = "bankAccountName is required!")
    @Size(max = 40, message = "bankAccountName max 40 character!")
    private String bankAccountName;

    @Range(min= -999999999999L, max = 999999999999L, message = "itemTotalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long itemTotalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialItemTotalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialItemTotalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "itemTotalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long itemTotalForTenPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialItemTotalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialItemTotalForTenPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "consumptionTaxTotalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long consumptionTaxTotalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialConsumptionTaxTotalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialConsumptionTaxTotalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "consumptionTaxTotalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long consumptionTaxTotalForTenPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialConsumptionTaxTotalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialConsumptionTaxTotalForTenPercent;

    @NotNull(message = "itemTotal is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "itemTotal min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long itemTotal;

    @NotNull(message = "initialItemTotal is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialItemTotal min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialItemTotal;

    @Range(min= -999999999999L, max = 999999999999L, message = "consumptionTaxTotal min > -1000.000.000.000 and max < 1000.000.000.000!")
    @NotNull(message = "consumptionTaxTotal must not be null!")
    private Long consumptionTaxTotal;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialConsumptionTaxTotal min > -1000.000.000.000 and max < 1000.000.000.000!")
    @NotNull(message = "initialConsumptionTaxTotal must not be null!")
    private Long initialConsumptionTaxTotal;

    @Range(min= -999999999999L, max = 999999999999L, message = "totalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long totalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialTotalForEightPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialTotalForEightPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "totalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long totalForTenPercent;

    @Range(min= -999999999999L, max = 999999999999L, message = "initialTotalForTenPercent min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialTotalForTenPercent;

    @NotNull(message = "total is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "total min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long total;

    @NotNull(message = "initialTotal is required!")
    @Range(min= -999999999999L, max = 999999999999L, message = "initialTotal min > -1000.000.000.000 and max < 1000.000.000.000!")
    private Long initialTotal;

    private Boolean isTemp;

    private String comment;

    private String editShainCd;

    private String editShainName;

    private Long startedEditAt;

    @Valid
    private ApprovalFlowDetailsRequest approvalFlowDetailsRequest;

    private MstApprovalFlowsResponse mstApprovalFlows;

    private List<FileModel> fileModelList;

    private MstRequestStatusesResponse mstRequestStatusesResponse;

    private LocalDateTime updatedAt;

    private Byte stepNumber;

    private String requestTypeName;

    private String createBy;

    private LocalDateTime updatedAtAfterAccountingCheck;

    private LocalDateTime requestedAt;

    private String settlementNumber;

    private RequestNumberResponse requestNumberResponse;

}
