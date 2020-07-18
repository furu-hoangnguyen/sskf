package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sskf.model.request.AccountReceivablesDetailRequest;
import sskf.model.request.RequestModel;


import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RequestAccountsReceivablesResponse {

    private Long cd;

    private Date targetOn;

    private String purpose;

    private String purposeOfOthers;

    private Byte commissionType;

    private String remarks;

}
