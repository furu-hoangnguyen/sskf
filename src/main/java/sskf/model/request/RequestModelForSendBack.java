package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class RequestModelForSendBack {

    @NotNull(message = "requestCd is required!")
    private Long requestCd;

    @NotNull(message = "targetStep is required!")
    @Min(value = 1, message = "targetStep has must better 0!")
    private Integer targetStep;

    private String comment;
}
