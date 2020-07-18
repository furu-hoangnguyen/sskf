package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestExhibitionPromotionsEntity;

@Repository
public interface RequestExhibitionPromotionsRepository extends JpaRepository<RequestExhibitionPromotionsEntity, Long> {
}
