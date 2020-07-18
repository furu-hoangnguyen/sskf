package sskf.service;

import org.springframework.data.domain.Page;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstStoreResponse;

import java.util.List;

public interface MstStoreServices {
    Page<MstStoreResponse> getStores(BaseSearchRequest searchRequest);

    List<MstStoreResponse> listStores(BaseSearchRequest searchRequest);
}
