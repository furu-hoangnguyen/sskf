package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestMannequinPromotionsEntity;

@Repository
public interface RequestMannequinPromotionsRepository extends JpaRepository<RequestMannequinPromotionsEntity, Long>, SearchRSQLRepository<RequestMannequinPromotionsEntity> {
}
