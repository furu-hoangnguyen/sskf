package sskf.service;

import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.request.ExhibitionPromotionsRequest;

public interface HoldingsForRequestExhibitionPromotionService {
    void setHoldingsForRequestExhibition(RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity, ExhibitionPromotionsRequest exhibitionPromotionsRequest);
}
