package sskf.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class BaseCustomRepositoryImpl<T> implements UserLogCustomRepository<T> {

    @Autowired
    protected EntityManager manager;

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> findByQuery(CriteriaQuery<T> criteriaQuery, Root<T> root, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        criteriaQuery.select((Selection<? extends T>) criteriaBuilder.count(root));
        Long count = (Long) manager.createQuery(criteriaQuery).getSingleResult();
        criteriaQuery.select(root);
        org.springframework.data.domain.Sort.Order sort = null;
        Iterator<Sort.Order> orderIterator = pageable.getSort().iterator();
        List<Order> orderList = new ArrayList<>();
        while(orderIterator.hasNext()) {
            sort = orderIterator.next();
            String sortfield = sort.getProperty();
            Order order = null;
            Expression<String> obj = null;
            Expression<String> expression = null;
            if (!sortfield.contains(".")) {
                obj = root.get(sort.getProperty());
                expression = null;
                if (obj.getJavaType().isInstance(java.util.Date.class)) {
                    expression = criteriaBuilder.lower((Expression<String>) obj);
                } else
                    expression = obj;
                order = criteriaBuilder.asc(expression);
                if (sort.isDescending()) {
                    order = criteriaBuilder.desc(expression);
                }
            } else {
                Set<Join<T, ?>> joins = root.getJoins();
                for (Join<T, ?> join : joins) {
                    String className = join.getJavaType().getSimpleName().toLowerCase();
                    String[] field = sortfield.split("\\.");
                    if (className.equals(field[0])) {
                        obj = join.get(field[1]);
                        expression = null;
                        if (obj.getJavaType().isInstance(java.util.Date.class)) {
                            expression = criteriaBuilder.lower((Expression<String>) obj);
                        } else
                            expression = obj;
                        order = criteriaBuilder.asc(expression);
                        if (sort.isDescending()) {
                            order = criteriaBuilder.desc(expression);
                        }
                    }
                }
            }
            orderList.add(order);
        }
        criteriaQuery.orderBy(orderList);
        TypedQuery<T> typedQuery = manager.createQuery(criteriaQuery);
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        typedQuery.setFirstResult(firstResult);
        typedQuery.setMaxResults(pageable.getPageSize());

        List<T> pageList = typedQuery.getResultList();
        return new PageImpl<>(pageList, pageable, count);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        Query query = manager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}