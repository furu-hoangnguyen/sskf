package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestsEntity;

import java.util.List;

public interface OperationHistoriesService {

    OperationHistoriesEntity setOperationHistoriesForRequests(RequestsEntity requestsEntity, String commentActionName, String contentComment, Boolean isDeputy, Byte stepNumber);

    OperationHistoriesEntity setOperationHistoriesBySystem(RequestsEntity requestsEntity, String commentActionName, String contentComment);

    OperationHistoriesEntity setOperationHistoriesForApprovalFlowsDeputy(RequestsEntity requestsEntity, String commentAction, Byte stepNumber, String shainNm);

    List<OperationHistoriesEntity> updateParent(Long requestsEntityCd, String commentActionChild, OperationHistoriesEntity parent);

    List<OperationHistoriesEntity> list(BaseSearchRequest baseSearchRequest);

    List<OperationHistoriesEntity> saveList(List<OperationHistoriesEntity> operationHistoriesEntityList);

    String getCreatedBy(Long requestCd);
}
