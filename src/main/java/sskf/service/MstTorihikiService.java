package sskf.service;

import org.springframework.data.domain.Page;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstTorihikiRespone;

import java.util.List;

public interface MstTorihikiService {
    List<String> listTorihikiRnmForSearch(BaseSearchRequest searchRequest);

    Page<MstTorihikiRespone> listTorihiki(BaseSearchRequest searchRequest);
}
