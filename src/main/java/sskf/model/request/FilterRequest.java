package sskf.model.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    // request_numbers.request_cd, Prefix matching: LIKE 'pre%', always, #21
    private String applyNumber;

    // requests.settlement_number, Prefix matching, always, #22
    private String settlementNumber;

    // requests.mst_request_type_cd, search equal, always, #23
    private String requestType;

    // (Confirm person) or (Applicant) or (Approver), Partial matching: LIKE '%patial%', page 4, #24
    private String relatedPerson;

    // request_accounts_receivable_details.shain_nm, Partial matching, page 3, 4, 5, 6, #25
    private String confirmPerson;

    // approvalflow_details.shain_nm where approvalflow_details.step_number=1, Partial matching, page 3, 4, 5, 6, #26
    private String applicant;

    // approvalflow_details.shain_nm where approvalflow_details.step_number in (2,3,4,5), Partial matching, page 3, 4, 5, 6, #27
    private String approver;

    // mst_request_statuses.name and 差し戻し/Send back (requests.is_sent_back), search equal, always, #28
    private String status;

    // requests.torihiki_nm, Partial matching, always, #29
    private String supplierName;

    // requests.requested_at, Search between, always, #30
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private java.util.Date appliedAtMin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private java.util.Date appliedAtMax;

    // requests.scheduled_payment_on, Search between, always, #31
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date paymentScheduledDateMin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date paymentScheduledDateMax;

    // requests.updated_status_at < now() - numberOfStagnancyDay, #32
    private Integer numberOfStagnancyDay;

    // requests.billing_amount, Search between, always, #33
    private Long amountMin;
    private Long amountMax;

}
