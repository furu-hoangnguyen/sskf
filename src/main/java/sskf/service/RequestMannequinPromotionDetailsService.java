package sskf.service;

import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.MannequinPromotionsRequest;

public interface RequestMannequinPromotionDetailsService {

    void setRequestMannequinPromotionDetailsForMannequin(RequestMannequinPromotionsEntity requestMannequinPromotionsEntity, MannequinPromotionsRequest mannequinPromotionsRequest);
}
