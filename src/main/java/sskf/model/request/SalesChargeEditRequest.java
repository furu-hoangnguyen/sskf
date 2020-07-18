package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SalesChargeEditRequest {

    private Long requestCd;

    private String comment;

    @NotEmpty(message = "salesChargeEditItemRequests is required!")
    List<SalesChargeEditItemRequest> salesChargeEditItemRequests;
}
