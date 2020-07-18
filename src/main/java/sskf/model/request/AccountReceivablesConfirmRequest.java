package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AccountReceivablesConfirmRequest {

    private Long requestCd;

    private String comment;

    @NotEmpty(message = "itemNumberConfirmed is required!")
    List<SalesChargeEditItemRequest> itemNumberConfirmed;
}
