package sskf.service;

import org.springframework.data.domain.Page;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ShainEntity;
import sskf.model.response.ShainResponse;

import java.util.List;

public interface ShainService {

    ShainResponse getInformation();

    Page<ShainResponse> listShains(BaseSearchRequest baseSearchRequest);

    Page<ShainResponse> listShainsMaster(BaseSearchRequest baseSearchRequest);

    String getUserNameByToken();

    String getBumonByToken();

    List<ShainEntity> getListShains(BaseSearchRequest baseSearchRequest);

    ShainEntity getLoggedInShainEntity();

    List<ShainResponse> listShainForApproval(Byte stepNumber, String bumonCd, Long requestCd);

    String getCurrentUserBumonCd ();

    ShainResponse updateShainInformation(ShainResponse shainResponse);

}
