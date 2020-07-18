package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
public class ImplementationStoresForRequestMannequinPromotionRequest {

    private Long cd;

    @Length(max = 50, message = "storeNameOfEvent has to letter than or equal 50 character!")
    private String storeNameOfEvent;

    @NotNull(message = "runEventOn is required!")
    private Date runEventOn;

    private Long sortNumber;
}
