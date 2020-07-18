package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class RequestModelForApproval {

    @NotNull(message = "requestCd is required!")
    private Long requestCd;

    private String comment;

    private Byte approvingStep;
}
