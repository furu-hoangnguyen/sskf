package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstHinmokuCategoriesEntity;

import java.util.List;

@Repository
public interface MstHinmokuCategoriesRepository extends JpaRepository<MstHinmokuCategoriesEntity, Integer> {
    List<MstHinmokuCategoriesEntity> findByCategoryType(Byte category);
}
