package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SalesChargeEditItemRequest {

    private Long accountReceivableDetailCd;

    private String shainCdItem;

}
