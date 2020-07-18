package sskf.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilterSelectResponse {

    private List<MstRequestTypesResponse> mstRequestTypesResponses;

    private List<MstRequestStatusesResponse> mstRequestStatusesResponses;

}
