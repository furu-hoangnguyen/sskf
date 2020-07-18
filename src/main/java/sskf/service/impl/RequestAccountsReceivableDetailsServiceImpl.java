package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.DetailsForAccountsReceivablesMapper;
import sskf.mapper.DetailsForPromotionalExpensesMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;
import sskf.model.entity.DetailsForPromotionalExpensesEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.AccountReceivablesConfirmRequest;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.DetailsForAccountsReceivablesRequest;
import sskf.model.request.DetailsForPromotionalExpensesRequest;
import sskf.repository.CommentsForDetailsRepository;
import sskf.repository.DetailsForAccountsReceivablesRepository;
import sskf.repository.DetailsForPromotionalExpensesRepository;
import sskf.repository.MstBumonRepository;
import sskf.repository.RequestAccountsReceivableDetailsRepository;
import sskf.repository.ShainRepository;
import sskf.service.CommentsForDetailsService;
import sskf.service.RequestAccountsReceivableDetailsService;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestAccountsReceivableDetailsServiceImpl extends BaseServiceHasSearchRSQL<RequestAccountsReceivableDetailsEntity> implements RequestAccountsReceivableDetailsService {

    @Autowired
    private DetailsForAccountsReceivablesMapper detailsForAccountsReceivablesMapper;

    @Autowired
    private DetailsForAccountsReceivablesRepository detailsForAccountsReceivablesRepository;

    @Autowired
    private DetailsForPromotionalExpensesMapper detailsForPromotionalExpensesMapper;

    @Autowired
    private DetailsForPromotionalExpensesRepository detailsForPromotionalExpensesRepository;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainService shainService;

    @Autowired
    private MstBumonRepository mstBumonRepository;

    @Autowired
    private CommentsForDetailsService commentsForDetailsService;

    @Autowired
    private CommentsForDetailsRepository commentsForDetailsRepository;

    @Autowired
    private RequestAccountsReceivableDetailsRepository requestAccountsReceivableDetailsRepository;


    @Override
    @Transactional
    public void create(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, AccountReceivablesRequest accountReceivablesRequest, String status) {
        try {
            log.info("Begin service: create RequestAccountsReceivableDetailsEntity");
            ShainEntity createdBy = shainRepository.findByShainCd(shainService.getUserNameByToken());
            if (CollectionUtil.isNotEmpty(accountReceivablesRequest.getDetailsForAccountsReceivablesRequestList()))
                updateDetailsForAccountsReceivables(requestAccountsReceivablesEntity, accountReceivablesRequest.getDetailsForAccountsReceivablesRequestList(), createdBy, status);
            if (CollectionUtil.isNotEmpty(accountReceivablesRequest.getDetailsForPromotionalExpensesRequestTax8PercentList()))
                updateDetailForPromotionExpenses(requestAccountsReceivablesEntity, accountReceivablesRequest.getDetailsForPromotionalExpensesRequestTax8PercentList(), createdBy, status);
            if (CollectionUtil.isNotEmpty(accountReceivablesRequest.getDetailsForPromotionalExpensesRequestTax10PercentList()))
                updateDetailForPromotionExpenses(requestAccountsReceivablesEntity, accountReceivablesRequest.getDetailsForPromotionalExpensesRequestTax10PercentList(), createdBy, status);
            if (CollectionUtil.isNotEmpty(accountReceivablesRequest.getDetailsForPromotionalExpensesRequestLogisticFeesList()))
                updateDetailForPromotionExpenses(requestAccountsReceivablesEntity, accountReceivablesRequest.getDetailsForPromotionalExpensesRequestLogisticFeesList(), createdBy, status);
        } finally {
            log.info("End service: create RequestAccountsReceivableDetailsEntity");
        }
    }

    @Override
    @Transactional
    public void update(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, AccountReceivablesRequest accountReceivablesRequest, String status) {
        try {
            log.info("Begin service: update RequestAccountsReceivableDetailsEntity");

            if (CollectionUtil.isNotEmpty(accountReceivablesRequest.getAccountReceivableDetailIsDeleted())) {
                List<RequestAccountsReceivableDetailsEntity> itemsDeleted = requestAccountsReceivableDetailsRepository.findAllById(accountReceivablesRequest.getAccountReceivableDetailIsDeleted());
                for (RequestAccountsReceivableDetailsEntity itemDelete : itemsDeleted) {
                    itemDelete.setIsDeleted(Constants.IS_DELETED);
                }
                requestAccountsReceivableDetailsRepository.saveAll(itemsDeleted);
            }
            create(requestAccountsReceivablesEntity, accountReceivablesRequest, status);
        } finally {
            log.info("End service: update RequestAccountsReceivableDetailsEntity");
        }
    }

    @Override
    @Transactional
    public void confirm(AccountReceivablesConfirmRequest receivablesConfirmRequest) {
        try {
            log.info("Begin service: confirm RequestAccountsReceivableDetailsEntity");

            if (CollectionUtil.isNotEmpty(receivablesConfirmRequest.getItemNumberConfirmed())) {
                List<Long> receivableConfirmCd = receivablesConfirmRequest.getItemNumberConfirmed().stream().map(e -> e.getAccountReceivableDetailCd()).collect(Collectors.toList());
                List<RequestAccountsReceivableDetailsEntity> receivableConfirm = requestAccountsReceivableDetailsRepository.findAllById(receivableConfirmCd);
                for (RequestAccountsReceivableDetailsEntity receivableRequest : receivableConfirm) {
                    receivableRequest.setIsChecked((byte) 1);
                }
                requestAccountsReceivableDetailsRepository.saveAll(receivableConfirm);
            }
        } finally {
            log.info("End service: confirm RequestAccountsReceivableDetailsEntity");
        }
    }

    @Override
    public List<RequestAccountsReceivableDetailsEntity> list(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Begin service: list RequestAccountsReceivableDetailsEntity");
            List<RequestAccountsReceivableDetailsEntity> result = listRSQL(requestAccountsReceivableDetailsRepository, baseSearchRequest, RequestAccountsReceivableDetailsEntity.class);
            return result;
        } finally {
            log.info("End service: list RequestAccountsReceivableDetailsEntity");
        }

    }

    private void updateDetailsForAccountsReceivables(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, List<DetailsForAccountsReceivablesRequest> detailsForAccountsReceivablesRequestList,
                                                     ShainEntity createdBy, String status) {

        List<Long> cdUpdated = new ArrayList<>();
        List<String> listBummonCd = new ArrayList<>();
        List<String> listShainCd = new ArrayList<>();
        List<DetailsForAccountsReceivablesEntity> detailsEntityInsert = new ArrayList<>();
        DetailsForAccountsReceivablesEntity newEntity;
        HashMap<String, String> itemNews = new HashMap<>();
        for (DetailsForAccountsReceivablesRequest detail : detailsForAccountsReceivablesRequestList) {
            if (ObjectUtil.isEmpty(detail.getCd())) {
                newEntity = detailsForAccountsReceivablesMapper.toEntity(detail, requestAccountsReceivablesEntity, createdBy);
                detailsEntityInsert.add(newEntity);
                itemNews.put(detail.getItemNumber(), detail.getItemNumber());
            } else {
                cdUpdated.add(detail.getCd());
            }
            listBummonCd.add(detail.getBumonCd());
            listShainCd.add(detail.getShainCd());
        }

        HashMap<String, DetailsForAccountsReceivablesRequest> detailsForAccountsReceivablesRequestHashMap = detailsForAccountsReceivablesRequestList.stream().collect(Collectors.toMap(DetailsForAccountsReceivablesRequest::getItemNumber, detailForAccount -> detailForAccount, (a, b) -> b, HashMap::new));

        List<MstBumonEntity> mstBumonEntityList = mstBumonRepository.findAllById(listBummonCd);
        HashMap<String, MstBumonEntity> bumonMap = mstBumonEntityList.stream().collect(Collectors.toMap(MstBumonEntity::getBumonCd, bumon -> bumon, (a, b) -> b, HashMap::new));

        List<ShainEntity> shainEntityList = shainRepository.findAllById(listShainCd);
        HashMap<String, ShainEntity> shainMap = shainEntityList.stream().collect(Collectors.toMap(ShainEntity::getShainCd, shain -> shain, (a, b) -> b, HashMap::new));

        List<DetailsForAccountsReceivablesEntity> entityUpdated = detailsForAccountsReceivablesRepository.findAllById(cdUpdated);

        if (CollectionUtil.isNotEmpty(entityUpdated)) {
            entityUpdated.addAll(detailsEntityInsert);
        } else {
            entityUpdated = detailsEntityInsert;
        }

        List<CommentsForDetailsEntity> commentsForDetailsEntityForItemUpdated = new ArrayList<>();
        for (DetailsForAccountsReceivablesEntity entity : entityUpdated) {
            RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = entity.getRequestAccountsReceivableDetailsEntity();
            String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
            DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest = detailsForAccountsReceivablesRequestHashMap.get(itemNumber);

            if (ObjectUtil.isNotEmpty(entity.getCd())) {
                entity = detailsForAccountsReceivablesMapper.toEntityUpdated(entity, detailsForAccountsReceivablesRequest);
                requestAccountsReceivableDetailsEntity = entity.getRequestAccountsReceivableDetailsEntity();
            } else {
                if (status.equals("作成中") || status.equals("確認待ち")) {
                    requestAccountsReceivableDetailsEntity.setIsChecked((byte) 0);
                } else {
                    requestAccountsReceivableDetailsEntity.setIsChecked((byte) 1);
                }
            }
            ShainEntity shainForItem = shainMap.get(detailsForAccountsReceivablesRequest.getShainCd());
            if (ObjectUtils.isEmpty(shainForItem)) {
                throw new SSKFException("Shain", "Shain for item: " + itemNumber + " invalid!");
            }
            requestAccountsReceivableDetailsEntity.setShainEntity(shainForItem);

            MstBumonEntity bumonForItem = bumonMap.get(detailsForAccountsReceivablesRequest.getBumonCd());
            if (ObjectUtils.isEmpty(bumonForItem)) {
                throw new SSKFException("Bumon", "Bumon for item: " + itemNumber + " invalid!");
            }
            requestAccountsReceivableDetailsEntity.setMstBumonEntity(bumonForItem);
            requestAccountsReceivableDetailsEntity.setBumonNm(bumonForItem.getBumonNm());
            requestAccountsReceivableDetailsEntity.setStoreGCd(detailsForAccountsReceivablesRequest.getStoreGCd());
            entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
            entity.setMishuLimit(detailsForAccountsReceivablesRequest.getMishuLimit());
        }
        entityUpdated = detailsForAccountsReceivablesRepository.saveAll(entityUpdated);
        for (DetailsForAccountsReceivablesEntity detail : entityUpdated) {
            RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detail.getRequestAccountsReceivableDetailsEntity();
            String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
            if (!ObjectUtils.isEmpty(itemNews.get(itemNumber)) || status.equals("作成中")) {
                DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest = detailsForAccountsReceivablesRequestHashMap.get(itemNumber);
                if (CollectionUtil.isNotEmpty(detailsForAccountsReceivablesRequest.getCommentDetailsRequests()))  {
                    Set<CommentsForDetailsEntity> commentsForDetailsEntities = commentsForDetailsService.createByRequestAccountReceivable(detailsForAccountsReceivablesRequest.getCommentDetailsRequests(), createdBy,
                            detail.getRequestAccountsReceivableDetailsEntity());
                    commentsForDetailsEntityForItemUpdated.addAll(commentsForDetailsEntities);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(commentsForDetailsEntityForItemUpdated))
            commentsForDetailsRepository.saveAll(commentsForDetailsEntityForItemUpdated);

    }

    private void updateDetailForPromotionExpenses(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestList,
                                                  ShainEntity createdBy, String status) {

        List<Long> cdUpdated = new ArrayList<>();
        List<String> listBummonCd = new ArrayList<>();
        List<String> listShainCd = new ArrayList<>();
        List<DetailsForPromotionalExpensesEntity> detailsEntityInsert = new ArrayList<>();
        DetailsForPromotionalExpensesEntity newEntity;
        HashMap<String, String> itemNews = new HashMap<>();
        for (DetailsForPromotionalExpensesRequest detail : detailsForPromotionalExpensesRequestList) {
            if (ObjectUtils.isEmpty(detail.getCd())) {
                newEntity = detailsForPromotionalExpensesMapper.toEntity(detail, requestAccountsReceivablesEntity, createdBy);
                detailsEntityInsert.add(newEntity);
                itemNews.put(detail.getItemNumber(), detail.getItemNumber());
            } else {
                cdUpdated.add(detail.getCd());
            }
            listBummonCd.add(detail.getBumonCd());
            listShainCd.add(detail.getShainCd());
        }

        HashMap<String, DetailsForPromotionalExpensesRequest> detailsForAccountsReceivablesRequestHashMap = detailsForPromotionalExpensesRequestList.stream().collect(Collectors.toMap(DetailsForPromotionalExpensesRequest::getItemNumber, detailForAccount -> detailForAccount, (a, b) -> b, HashMap::new));

        List<MstBumonEntity> mstBumonEntityList = mstBumonRepository.findAllById(listBummonCd);
        HashMap<String, MstBumonEntity> bumonMap = mstBumonEntityList.stream().collect(Collectors.toMap(MstBumonEntity::getBumonCd, bumon -> bumon, (a, b) -> b, HashMap::new));

        List<ShainEntity> shainEntityList = shainRepository.findAllById(listShainCd);
        HashMap<String, ShainEntity> shainMap = shainEntityList.stream().collect(Collectors.toMap(ShainEntity::getShainCd, shain -> shain, (a, b) -> b, HashMap::new));

        List<DetailsForPromotionalExpensesEntity> entityUpdated = detailsForPromotionalExpensesRepository.findAllById(cdUpdated);

        if (CollectionUtil.isNotEmpty(entityUpdated)) {
            entityUpdated.addAll(detailsEntityInsert);
        } else {
            entityUpdated = detailsEntityInsert;
        }

        List<CommentsForDetailsEntity> commentsForDetailsEntityForItemUpdated = new ArrayList<>();
        for (DetailsForPromotionalExpensesEntity entity : entityUpdated) {
            RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = entity.getRequestAccountsReceivableDetailsEntity();
            String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
            DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = detailsForAccountsReceivablesRequestHashMap.get(itemNumber);

            if (!ObjectUtils.isEmpty(entity.getCd())) {
                entity = detailsForPromotionalExpensesMapper.toEntityUpdated(entity, detailsForPromotionalExpensesRequest);
                requestAccountsReceivableDetailsEntity= entity.getRequestAccountsReceivableDetailsEntity();
            } else {
                if (status.equals("作成中") || status.equals("確認待ち")) {
                    requestAccountsReceivableDetailsEntity.setIsChecked((byte) 0);
                } else {
                    requestAccountsReceivableDetailsEntity.setIsChecked((byte) 1);
                }
            }

            ShainEntity shainForItem = shainMap.get(detailsForPromotionalExpensesRequest.getShainCd());
            if (ObjectUtils.isEmpty(shainForItem)) {
                throw new SSKFException("Shain", "Shain for item: " + itemNumber + " invalid!");
            }
            requestAccountsReceivableDetailsEntity.setShainEntity(shainForItem);

            MstBumonEntity bumonForItem = bumonMap.get(detailsForPromotionalExpensesRequest.getBumonCd());
            if (ObjectUtils.isEmpty(bumonForItem)) {
                throw new SSKFException("Bumon", "Bumon for item: " + itemNumber + " invalid!");
            }
            requestAccountsReceivableDetailsEntity.setMstBumonEntity(bumonForItem);
            requestAccountsReceivableDetailsEntity.setBumonNm(bumonForItem.getBumonNm());
            requestAccountsReceivableDetailsEntity.setStoreGCd(detailsForPromotionalExpensesRequest.getStoreGCd());
            entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
        }
        entityUpdated = detailsForPromotionalExpensesRepository.saveAll(entityUpdated);
        for (DetailsForPromotionalExpensesEntity detail : entityUpdated) {
            RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detail.getRequestAccountsReceivableDetailsEntity();
            String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
            if (!ObjectUtils.isEmpty(itemNumber) || status.equals("作成中")) {
                DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = detailsForAccountsReceivablesRequestHashMap.get(itemNumber);
                if (CollectionUtil.isNotEmpty(detailsForPromotionalExpensesRequest.getCommentDetailsRequests())) {
                    Set<CommentsForDetailsEntity> commentsForDetailsEntities = commentsForDetailsService.createByRequestAccountReceivable(detailsForPromotionalExpensesRequest.getCommentDetailsRequests(), createdBy, detail.getRequestAccountsReceivableDetailsEntity());
                    commentsForDetailsEntityForItemUpdated.addAll(commentsForDetailsEntities);
                }
            }


        }
        if (CollectionUtil.isNotEmpty(commentsForDetailsEntityForItemUpdated))
            commentsForDetailsRepository.saveAll(commentsForDetailsEntityForItemUpdated);
    }
}