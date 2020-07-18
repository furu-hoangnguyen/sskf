package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sskf.exception.SSKFException;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.repository.MstRequestCommentActionsRepository;
import sskf.repository.OperationHistoriesRepository;
import sskf.repository.ShainRepository;
import sskf.service.OperationHistoriesService;
import sskf.service.ShainService;
import sskf.util.StringUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class OperationHistoriesServiceImpl extends BaseServiceHasSearchRSQL<OperationHistoriesEntity> implements OperationHistoriesService {

    @Autowired
    private MstRequestCommentActionsRepository mstRequestCommentActionsRepository;

    @Autowired
    private OperationHistoriesRepository operationHistoriesRepository;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainService shainService;

    @Override
    @Transactional
    public OperationHistoriesEntity setOperationHistoriesForRequests(RequestsEntity requestsEntity, String commentActionName, String contentComment, Boolean isDeputy, Byte stepNumber) {
        try {
            log.info("End service: setOperationHistoriesForRequests");
            String userLogin = shainService.getUserNameByToken();
            MstRequestCommentActionsEntity mstRequestCommentActionsEntity = mstRequestCommentActionsRepository.findByName(commentActionName);
            if (ObjectUtils.isEmpty(mstRequestCommentActionsEntity))
                throw new SSKFException("404", "Not found comment action!");

            OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
            if (StringUtil.isNotEmpty(contentComment)) {
                operationHistoriesEntity.setComment(contentComment);
            } else {
                operationHistoriesEntity.setComment(mstRequestCommentActionsEntity.getAutoComment());
            }
            operationHistoriesEntity.setMstRequestCommentActionsEntity(mstRequestCommentActionsEntity);
            operationHistoriesEntity.setRequestsEntity(requestsEntity);
            ShainEntity shainEntity = shainRepository.findByShainCd(userLogin);
            operationHistoriesEntity.setShainEntity(shainEntity);
            operationHistoriesEntity.setShainNm(shainEntity.getShainNm());
            operationHistoriesEntity.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
            operationHistoriesEntity.setIsDoneBySystem((byte) 0);
            operationHistoriesEntity.setStepNumber(stepNumber);
            if (isDeputy) {
                operationHistoriesEntity.setIsDeputy(true);
            } else {
                operationHistoriesEntity.setIsDeputy(false);
            }
            operationHistoriesEntity = operationHistoriesRepository.save(operationHistoriesEntity);
            Set<OperationHistoriesEntity> operationHistoriesEntitySet = new HashSet<>();
            operationHistoriesEntitySet.add(operationHistoriesEntity);
            requestsEntity.setOperationHistoriesEntities(operationHistoriesEntitySet);
            return operationHistoriesEntity;
        } finally {
            log.info("End service: setOperationHistoriesForRequests");
        }
    }

    @Override
    @Transactional
    public OperationHistoriesEntity setOperationHistoriesBySystem(RequestsEntity requestsEntity, String commentActionName, String contentComment) {
        try {
            log.info("End service: setOperationHistoriesForRequests");
            MstRequestCommentActionsEntity mstRequestCommentActionsEntity = mstRequestCommentActionsRepository.findByName(commentActionName);
            if (ObjectUtils.isEmpty(mstRequestCommentActionsEntity))
                throw new SSKFException("404", "Not found comment action!");

            OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
            if (StringUtil.isNotEmpty(contentComment)) {
                operationHistoriesEntity.setComment(contentComment);
            } else {
                operationHistoriesEntity.setComment(mstRequestCommentActionsEntity.getAutoComment());
            }
            operationHistoriesEntity.setMstRequestCommentActionsEntity(mstRequestCommentActionsEntity);
            operationHistoriesEntity.setRequestsEntity(requestsEntity);
            operationHistoriesEntity.setShainNm("システム");
            operationHistoriesEntity.setIsDoneBySystem((byte) 1);
            operationHistoriesEntity.setIsDeputy(false);
            operationHistoriesEntity = operationHistoriesRepository.save(operationHistoriesEntity);
            Set<OperationHistoriesEntity> operationHistoriesEntitySet = new HashSet<>();
            operationHistoriesEntitySet.add(operationHistoriesEntity);
            requestsEntity.setOperationHistoriesEntities(operationHistoriesEntitySet);
            return operationHistoriesEntity;
        } finally {
            log.info("End service: setOperationHistoriesForRequests");
        }
    }

    @Override
    @Transactional
    public OperationHistoriesEntity setOperationHistoriesForApprovalFlowsDeputy(RequestsEntity requestsEntity, String commentAction, Byte stepNumber, String shainNm) {
        try {
            log.info("End service: setOperationHistoriesForApprovalFlows");
            MstRequestCommentActionsEntity mstRequestCommentActionsEntity = mstRequestCommentActionsRepository.findByName(commentAction);
            OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
            if (ObjectUtils.isEmpty(mstRequestCommentActionsEntity))
                throw new SSKFException("404", "Not found comment action!");
            operationHistoriesEntity.setMstRequestCommentActionsEntity(mstRequestCommentActionsEntity);
            operationHistoriesEntity.setRequestsEntity(requestsEntity);
            operationHistoriesEntity.setShainNm("システム");
            operationHistoriesEntity.setIsDoneBySystem((byte) 1);
            operationHistoriesEntity.setIsDeputy(false);
            String comment = mstRequestCommentActionsEntity.getAutoComment();
            String role = "";
            switch (stepNumber) {
                case (byte) 1:
                    role = "申請者";
                    break;
                case (byte) 2:
                    role = "第1承認者";
                    break;
                case (byte) 3:
                    role = "第2承認者";
                    break;
                case (byte )4:
                    role = "第3承認者";
                    break;
            }
            comment = comment.replace("$role$", role).replace("$shain_nm$", shainNm);
            operationHistoriesEntity.setComment(comment);
            operationHistoriesEntity = operationHistoriesRepository.save(operationHistoriesEntity);
            return operationHistoriesEntity;
        } finally {
            log.info("End service: setOperationHistoriesForApprovalFlows");
        }
    }


    @Override
    @Transactional
    public List<OperationHistoriesEntity> updateParent(Long requestsEntityCd, String commentActionChild, OperationHistoriesEntity parent) {
        List<OperationHistoriesEntity> children = operationHistoriesRepository.findByRequestsEntityCdAndMstRequestCommentActionsEntityNameAndParentOperationHistoriesEntityIsNull(requestsEntityCd, commentActionChild);
        for (OperationHistoriesEntity child : children) {
            child.setParentOperationHistoriesEntity(parent);
        }
        return operationHistoriesRepository.saveAll(children);
    }

    @Override
    public List<OperationHistoriesEntity> list(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("End service OperationHistoriesServiceImpl: list");
            List<OperationHistoriesEntity> operationHistoriesEntityList = listRSQL(operationHistoriesRepository, baseSearchRequest, OperationHistoriesEntity.class);
            return operationHistoriesEntityList;
        } finally {
            log.info("End service OperationHistoriesServiceImpl: list");
        }
    }

    @Override
    @Transactional
    public List<OperationHistoriesEntity> saveList(List<OperationHistoriesEntity> operationHistoriesEntityList) {
        return operationHistoriesRepository.saveAll(operationHistoriesEntityList);
    }

    @Override
    public String getCreatedBy(Long requestCd) {
        try {
            log.info("End service OperationHistoriesServiceImpl: getCreatedBy");
            String shainCd = operationHistoriesRepository.findCreatedBy(requestCd);
            return shainCd;
        } finally {
            log.info("End service OperationHistoriesServiceImpl: getCreatedBy");
        }
    }
}
