package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.CommentsForDetailsMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.basemodel.BaseTimeModel;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;
import sskf.repository.CommentsForDetailsRepository;
import sskf.repository.RequestAccountsReceivableDetailsRepository;
import sskf.repository.RequestsRepository;
import sskf.repository.ShainRepository;
import sskf.service.CommentsForDetailsService;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;
import sskf.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentsForDetailsServiceImpl extends BaseServiceHasSearchRSQL<CommentsForDetailsEntity> implements CommentsForDetailsService {

    @Autowired
    private CommentsForDetailsRepository commentsForDetailsRepository;

    @Autowired
    private CommentsForDetailsMapper commentsForDetailsMapper;

    @Autowired
    private RequestAccountsReceivableDetailsRepository requestAccountsReceivableDetailsRepository;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainService shainService;

    @Autowired
    private RequestsRepository requestsRepository;
    @Override
    public Set<CommentsForDetailsEntity> createByRequestAccountReceivable(List<CommentDetailsRequest> commentDetailsRequests, ShainEntity shainEntity,
                                                                          RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: create comment");
            Set<CommentsForDetailsEntity> commentsForDetailsEntities = new HashSet<>();
            if (CollectionUtil.isNotEmpty(commentDetailsRequests)) {
                commentsForDetailsEntities = commentDetailsRequests.stream().filter(commentDetailsRequest -> StringUtil.isNotEmpty(commentDetailsRequest.getComment())).map(commentsForDetailsMapper::toEntity).collect(Collectors.toSet());
                commentsForDetailsEntities.forEach(commentsForDetailsEntity -> setShainIntoComment(commentsForDetailsEntity, shainEntity, requestAccountsReceivableDetailsEntity));

            }
            return commentsForDetailsEntities;
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: create comment");
        }
    }

    @Override
    public List<Object> listOldComment(String storeGCd) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: listOldComment");
            return commentsForDetailsRepository.listOldComment(storeGCd);
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: listOldComment");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: delete");
            CommentsForDetailsEntity commentsForDetailsEntity = commentsForDetailsRepository.getOne(id);
            if (commentsForDetailsEntity.getIsCapableOfBeingDeleted() == 1) {
                commentsForDetailsEntity.setIsDeleted(Constants.IS_DELETED);
                commentsForDetailsRepository.save(commentsForDetailsEntity);
            } else {
                throw new SSKFException("Delete comment", "Can not delete comment!");
            }
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: delete");
        }
    }

    @Override
    public List<CommentDetailResponse> listByRequestAccount(Long requestAccountId) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: listByRequestAccount");
            List<CommentsForDetailsEntity> commentsForDetailsEntities = commentsForDetailsRepository.findByRequestAccountsReceivableDetailsEntityCdAndIsDeletedOrderByCdAsc(requestAccountId, Constants.NOT_DELETED);
           return commentsForDetailsEntities.stream().map(commentsForDetailsMapper :: toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: listByRequestAccount");
        }
    }

    @Override
    public List<CommentDetailResponse> list(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: list");
            String searchKey = "isDeleted==0;" + baseSearchRequest.getKeyword();
            baseSearchRequest.setKeyword(searchKey);
            List<CommentsForDetailsEntity> commentsForDetailsEntities = listRSQL(commentsForDetailsRepository, baseSearchRequest, CommentsForDetailsEntity.class);
            return commentsForDetailsEntities.stream().map(commentsForDetailsMapper :: toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: list");
        }
    }

    @Override
    public BaseTimeModel insertForItem(Long requestCd, List<CommentDetailsRequest>  commentDetailsRequests) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: insertForItem");
            RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
            if (ObjectUtil.isEmpty(requestsEntity)) {
                throw new SSKFException("requestsEntity", "requestsEntity not existed!");
            }

            List<Long> requestAccountsReceivableCd = new ArrayList<>();
            Map<Long, Byte>  commentDetailsRequestsMap = new HashMap<>();

            commentDetailsRequests.forEach(commentDetailsRequest -> {
                commentDetailsRequestsMap.put(commentDetailsRequest.getRequestAccountsReceivableDetailsCd(), commentDetailsRequest.getIsChecked());
                requestAccountsReceivableCd.add(commentDetailsRequest.getRequestAccountsReceivableDetailsCd());
            });

            List<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntitys = requestAccountsReceivableDetailsRepository.findAllById(requestAccountsReceivableCd);
            Map<Long, RequestAccountsReceivableDetailsEntity> map = new HashMap<>();
            for (RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity : requestAccountsReceivableDetailsEntitys) {
                map.put(requestAccountsReceivableDetailsEntity.getCd(), requestAccountsReceivableDetailsEntity);
                requestAccountsReceivableDetailsEntity.setIsChecked(commentDetailsRequestsMap.get(requestAccountsReceivableDetailsEntity.getCd()));
            }
            ShainEntity shain = shainRepository.getOne(shainService.getUserNameByToken());
            List<CommentsForDetailsEntity> comments = new ArrayList<>();
            for (CommentDetailsRequest comment : commentDetailsRequests) {
                CommentsForDetailsEntity entity = commentsForDetailsMapper.toEntity(comment);
                entity.setIsDeleted((byte) 0);
                entity.setIsDeputy((byte) 0);
                entity.setShainEntity(shain);
                entity.setShainNm(shain.getShainNm());
                entity.setBumonNm(shainService.getBumonByToken());
                RequestAccountsReceivableDetailsEntity requestOfComment = map.get(comment.getRequestAccountsReceivableDetailsCd());
                entity.setRequestAccountsReceivableDetailsEntity(requestOfComment);
                comments.add(entity);
            }
            commentsForDetailsRepository.saveAll(comments);
            LocalDateTime updatedAt = LocalDateTime.now();
            requestsEntity.setUpdatedAt(updatedAt);
            requestsEntity = requestsRepository.saveAndFlush(requestsEntity);
            updatedAt = requestsEntity.getUpdatedAt();
            return new BaseTimeModel(updatedAt, updatedAt);
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: insertForItem");
        }
    }

    @Override
    @Transactional
    public void updateIsCapableOfBeingDeleted(Long requestAccountsReceivablesCd, Long operationHistoriesEntitieCd) {
        try {
            log.info("Log begin service CommentsForDetailsServiceImpl: updateIsCapableOfBeingDeleted");
            commentsForDetailsRepository.updateIsCapableOfBeingDeleted(requestAccountsReceivablesCd, operationHistoriesEntitieCd);
        } finally {
            log.info("Log end service CommentsForDetailsServiceImpl: updateIsCapableOfBeingDeleted");
        }

    }

    private void setShainIntoComment(CommentsForDetailsEntity commentsForDetailsEntity, ShainEntity shainEntity,
                                     RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity) {
        commentsForDetailsEntity.setShainEntity(shainEntity);
        commentsForDetailsEntity.setShainNm(shainEntity.getShainNm());
        if (!ObjectUtils.isEmpty(shainEntity.getMstBumonEntity())) {
            commentsForDetailsEntity.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
        }
        if (!ObjectUtils.isEmpty(requestAccountsReceivableDetailsEntity)) {
            commentsForDetailsEntity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
        }
    }
}
