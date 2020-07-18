package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.HoldingsForRequestExhibitionPromotionEntity;

@Repository
public interface HoldingsForRequestExhibitionPromotionRepository extends JpaRepository<HoldingsForRequestExhibitionPromotionEntity, Long> {
}
