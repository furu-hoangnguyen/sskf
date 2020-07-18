package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;

@Repository
public interface RequestMannequinPromotionDetailsRepository extends JpaRepository<RequestMannequinPromotionDetailsEntity, Long> {
}
