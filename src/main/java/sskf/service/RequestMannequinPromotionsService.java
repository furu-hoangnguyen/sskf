package sskf.service;

import org.springframework.web.multipart.MultipartFile;
import sskf.model.request.MannequinPromotionsRequest;

public interface RequestMannequinPromotionsService {

    MannequinPromotionsRequest create(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files);

    MannequinPromotionsRequest updateCreate(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files);

    MannequinPromotionsRequest update(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files);

    MannequinPromotionsRequest getByRequestId(Long requestId, String mode) ;
}
