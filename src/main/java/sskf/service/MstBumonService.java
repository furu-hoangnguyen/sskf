package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.BumonResponse;

import java.util.List;

public interface MstBumonService {

    List<BumonResponse> listDepartmentForItem(BaseSearchRequest baseSearchRequest);

    List<BumonResponse> listDepartmentByStepNumber(Byte stepNumber);
}
