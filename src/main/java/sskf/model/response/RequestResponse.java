package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class RequestResponse {

    private Long cd;

    private String settlementNumber;

    private Byte isSentBack;

    private String mstTorihikiCd;

    private String torihikiNm;

    private Long billingAmount;

    private Long initialBillingAmount;

    private Date billingOn;

    private String paymentPlace;

    private String paymentOtherMethod;

    private Date scheduledPaymentOn;

    private Date paymentOn;

    private String paymentDestination;

    private String bankName;

    private Long itemTotalForEightPercent;

    private Long initialItemTotalForEightPercent;

    private Long itemTotalForTenPercent;

    private Long initialItemTotalForTenPercent;

    private Long consumptionTaxTotalForEightPercent;

    private Long initialConsumptionTaxTotalForEightPercent;

    private Long consumptionTaxTotalForTenPercent;

    private Long initialConsumptionTaxTotalForTenPercent;

    private Long itemTotal;

    private Long initialItemTotal;

    private Long consumptionTaxTotal;

    private Long initialConsumptionTaxTotal;

    private Long totalForEightPercent;

    private Long initialTotalForEightPercent;

    private Long totalForTenPercent;

    private Long initialTotalForTenPercent;

    private Long total;

    private Long initialTotal;

    private LocalDateTime updatedStatusAt;

    private LocalDateTime requestedAt;

    private LocalDateTime updatedAt;

    private RequestAccountsReceivablesResponse requestAccountsReceivablesResponse;

    private MstRequestTypesResponse mstRequestTypesResponse;

    private RequestExhibitionPromotionsResponse requestExhibitionPromotionsResponse;

    private RequestMannequinPromotionsResponse requestMannequinPromotionsResponse;

    private ApprovalFlowDetailsResponse approvalFlowDetailsResponse;

    private MstRequestStatusesResponse mstRequestStatusesResponse;

}
