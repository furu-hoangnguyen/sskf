package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AccountReceivablesDetailRequest {

    private Long accountReceivableDetailCd;

    @NotEmpty(message = "Item number is required!")
    private String itemNumber;

    @NotEmpty(message = "storeGNm number is required!")
    private String storeGNm;
    
    private String storeGCd;

    @NotEmpty(message = "shainCd number is required!")
    private String shainCd;

    private String shainNm;

    @NotEmpty(message = "bumonCd number is required!")
    private String bumonCd;

    private String bumonNm;

    private Long sortNumber;

    private Byte isDeleted;

    private Byte isChecked;

    private Boolean hasComment;

    private List<CommentDetailsRequest> commentDetailsRequests;

    public void removeComment() {
        this.commentDetailsRequests = null;
    }
}
