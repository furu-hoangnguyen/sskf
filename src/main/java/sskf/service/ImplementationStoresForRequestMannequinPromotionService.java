package sskf.service;

import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.MannequinPromotionsRequest;

public interface ImplementationStoresForRequestMannequinPromotionService {

    void setImplementationStoresForMannequin(RequestMannequinPromotionsEntity requestMannequinPromotionsEntity,
                                                         MannequinPromotionsRequest mannequinPromotionsRequest);

}
