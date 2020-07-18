package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstRequestTypesResponse;

import java.util.List;

public interface MstRequestTypeService {

    List<MstRequestTypesResponse> list(BaseSearchRequest searchRequest);
}
