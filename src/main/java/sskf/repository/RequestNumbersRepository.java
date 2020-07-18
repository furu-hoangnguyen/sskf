package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.RequestNumbersEntity;

@Repository
public interface RequestNumbersRepository extends JpaRepository<RequestNumbersEntity, Long> {
}
