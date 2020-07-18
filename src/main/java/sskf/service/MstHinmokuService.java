package sskf.service;

import org.springframework.data.domain.Page;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstHinmokuReponse;

import java.util.HashMap;

public interface MstHinmokuService {

    Page<MstHinmokuReponse> listPage(BaseSearchRequest searchRequest);

    HashMap<String, MstHinmokuReponse> listMap(BaseSearchRequest searchRequest);
}
