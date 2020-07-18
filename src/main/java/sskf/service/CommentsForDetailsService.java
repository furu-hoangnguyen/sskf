package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.basemodel.BaseTimeModel;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;

import java.util.List;
import java.util.Set;

public interface CommentsForDetailsService {

    Set<CommentsForDetailsEntity> createByRequestAccountReceivable(List<CommentDetailsRequest> commentDetailsRequests, ShainEntity shainEntity,
                                                                   RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity);

    List<Object> listOldComment(String storeGCd);

    void delete(Long id);

    List<CommentDetailResponse> listByRequestAccount(Long requestAccountId);

    List<CommentDetailResponse> list(BaseSearchRequest baseSearchRequest);

    BaseTimeModel insertForItem(Long requestCd, List<CommentDetailsRequest>  commentDetailsRequests);

    void updateIsCapableOfBeingDeleted(Long requestAccountsReceivablesCd, Long operationHistoriesEntitieCd);
}
