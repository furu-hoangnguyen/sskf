package sskf.repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserLogCustomRepository<T> {

    Page<T> findByQuery(CriteriaQuery<T> cq, Root<T> root, Pageable pageable);
    List<T> findAll(CriteriaQuery<T> criteriaQuery);

}
