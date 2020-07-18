package sskf.service;

import sskf.model.response.OperationHistoriesOfMstCommentResponse;

import java.util.List;

public interface MstRequestCommentActionsService {

    List<OperationHistoriesOfMstCommentResponse> list(Long requestCd);
}
