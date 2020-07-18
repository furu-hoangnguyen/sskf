package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sskf.model.entity.MstRequestCommentActionsEntity;

import java.util.List;
import java.util.Set;

public interface MstRequestCommentActionsRepository extends JpaRepository<MstRequestCommentActionsEntity, Long>, SearchRSQLRepository<MstRequestCommentActionsEntity> {
    MstRequestCommentActionsEntity findByName(String name);
    List<MstRequestCommentActionsEntity> findByNameIn(Set<String> names);
}
