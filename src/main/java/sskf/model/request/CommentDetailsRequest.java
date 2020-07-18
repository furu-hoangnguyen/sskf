package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentDetailsRequest {

    private Long requestAccountsReceivableDetailsCd;

    private String comment;

    private Byte isChecked;

    private Byte isDeputy;

}
