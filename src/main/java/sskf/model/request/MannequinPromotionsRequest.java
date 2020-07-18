package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MannequinPromotionsRequest {

    private Long cd;

    @Length(max = 50, message = "subject has to letter than or equal 50 character!")
    @NotEmpty
    private String subject;

    @Length(max = 40, message = "storeGNm has to letter than or equal 40 character!")
    @NotEmpty(message = "storeGNm is required!")
    private String storeGNm;

    @NotEmpty(message = "storeGCd is required!")
    private String storeGCd;

    private String contentOfImplementationStores;

    @Valid
    private RequestModel requestModel;

    @Valid
    private List<RequestMannequinPromotionDetailsRequest> requestMannequinPromotionDetailsRequestList;

    private List<Long> requestMannequinPromotionDetailsRequestIsDeletedList;

    @Valid
    private List<ImplementationStoresForRequestMannequinPromotionRequest> implementationStoresForRequestMannequinPromotionRequestList;

    private List<Long> implementationStoresForRequestMannequinPromotionRequestIsDeletedList;
}
