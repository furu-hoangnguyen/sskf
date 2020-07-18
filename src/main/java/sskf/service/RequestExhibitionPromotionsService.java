package sskf.service;

import org.springframework.web.multipart.MultipartFile;
import sskf.model.request.ExhibitionPromotionsRequest;

public interface RequestExhibitionPromotionsService {

    ExhibitionPromotionsRequest create(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files);

    ExhibitionPromotionsRequest updateCreate(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files);

    ExhibitionPromotionsRequest update(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files);

    ExhibitionPromotionsRequest getByRequestId(Long requestCd, String mode);
}
