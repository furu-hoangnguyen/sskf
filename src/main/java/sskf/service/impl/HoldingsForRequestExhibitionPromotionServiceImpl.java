package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sskf.mapper.HoldingsForRequestExhibitionPromotionMapper;
import sskf.model.entity.HoldingsForRequestExhibitionPromotionEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.model.request.HoldingsForRequestExhibitionPromotionRequest;
import sskf.repository.HoldingsForRequestExhibitionPromotionRepository;
import sskf.service.HoldingsForRequestExhibitionPromotionService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HoldingsForRequestExhibitionPromotionServiceImpl implements HoldingsForRequestExhibitionPromotionService {
    @Autowired
    private HoldingsForRequestExhibitionPromotionRepository repository;

    @Autowired
    private HoldingsForRequestExhibitionPromotionMapper mapper;

    @Override
    @Transactional
    public void setHoldingsForRequestExhibition(RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity, ExhibitionPromotionsRequest exhibitionPromotionsRequest) {
        try {
            log.info("Begin service HoldingsForRequestExhibitionPromotionServiceImpl: setHoldingsForRequestExhibition");
            if (CollectionUtil.isNotEmpty(exhibitionPromotionsRequest.getHoldingsForRequestExhibitionPromotionIsDeletedList())) {
                delete(exhibitionPromotionsRequest.getHoldingsForRequestExhibitionPromotionIsDeletedList());
            }
            List<HoldingsForRequestExhibitionPromotionRequest> holdings = exhibitionPromotionsRequest.getHoldingsForRequestExhibitionPromotionRequestList();
            if (CollectionUtil.isNotEmpty(holdings)) {
                HashMap<Long, HoldingsForRequestExhibitionPromotionRequest> holdingMap = holdings.stream().collect(Collectors.toMap(HoldingsForRequestExhibitionPromotionRequest::getCd, detailForAccount -> detailForAccount, (a, b) -> b, HashMap::new));
                List<Long> cdUpdated = new ArrayList<>();
                HoldingsForRequestExhibitionPromotionEntity newEntity = null;
                List<HoldingsForRequestExhibitionPromotionEntity> inserted = new ArrayList<>();
                for (HoldingsForRequestExhibitionPromotionRequest detail : holdings) {
                    if (ObjectUtil.isEmpty(detail.getCd())) {
                        newEntity = mapper.toEntity(detail);
                        newEntity.setRequestExhibitionPromotionsEntity(requestExhibitionPromotionsEntity);
                        inserted.add(newEntity);
                    } else {
                        cdUpdated.add(detail.getCd());
                    }
                }
                List<HoldingsForRequestExhibitionPromotionEntity> updateds = repository.findAllById(cdUpdated);
                for (HoldingsForRequestExhibitionPromotionEntity updated : updateds) {
                    HoldingsForRequestExhibitionPromotionRequest requestUpdated = holdingMap.get(updated.getCd());
                    mapper.toEntityUpdated(updated, requestUpdated);
                }
                if (CollectionUtil.isNotEmpty(inserted)) {
                    inserted.addAll(updateds);
                } else {
                    inserted = updateds;
                }
                repository.saveAll(inserted);
            }
        } finally {
            log.info("End service HoldingsForRequestExhibitionPromotionServiceImpl: setHoldingsForRequestExhibition");
        }
    }

    private void delete(List<Long> listCd) {
        List<HoldingsForRequestExhibitionPromotionEntity> isDeleted = repository.findAllById(listCd);
        repository.deleteAll(isDeleted);
    }
}
