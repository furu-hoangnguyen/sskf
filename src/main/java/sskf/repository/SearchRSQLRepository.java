package sskf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sskf.model.basemodel.BaseModel;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
public interface SearchRSQLRepository<T extends BaseModel> {

    Page<T> findByQuery(CriteriaQuery<T> cq, Root<T> root, Pageable pageable);

    List<T> findAll(CriteriaQuery<T> cq);
}
