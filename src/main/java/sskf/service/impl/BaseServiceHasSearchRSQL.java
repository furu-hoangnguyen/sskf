package sskf.service.impl;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import sskf.common.Constants;
import sskf.model.basemodel.BaseModel;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.repository.SearchRSQLRepository;
import sskf.rsql.jpa.JpaPredicateVisitor;
import sskf.util.ObjectUtil;
import sskf.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseServiceHasSearchRSQL<T extends BaseModel> {

    @Autowired
    private EntityManager entityManager;

    final public Page<T> searchRSQL(final SearchRSQLRepository<T> repository,
                                    final BaseSearchRequest searchRequest,
                                    final Class<T> c,
                                    final String defaultSortColumnName) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
        Root<T> root = criteriaQuery.from(c);
        if (StringUtil.isNotEmpty(searchRequest.getKeyword())) {
            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<>().defineRoot(root);
            String escapeKeyword = StringUtil.escapeRSQLCharacter(searchRequest.getKeyword());
            Node rootNode = new RSQLParser().parse(escapeKeyword);
            Predicate predicate = rootNode.accept(visitor, entityManager);
            criteriaQuery.where(predicate);
        }

        List<Sort.Order> orderList = buildOrderForPage(searchRequest.getSortField());
        Pageable pageable = searchRequest.createPageRequest(defaultSortColumnName, orderList);
        return repository.findByQuery(criteriaQuery, root, pageable);
    }

    final public List<T> listRSQL(final SearchRSQLRepository<T> repository,
                                  final BaseSearchRequest searchRequest,
                                  final Class<T> c) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
        Root<T> root = criteriaQuery.from(c);
        if (StringUtil.isNotEmpty(searchRequest.getKeyword())) {
            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<>().defineRoot(root);
            String escapeKeyword = StringUtil.escapeRSQLCharacter(searchRequest.getKeyword());
            Node rootNode = new RSQLParser().parse(escapeKeyword);
            Predicate predicate = rootNode.accept(visitor, entityManager);
            criteriaQuery.where(predicate);
        }
        List<Order> orderList = buildOrder(criteriaBuilder, root, searchRequest.getSortField());
        if (!CollectionUtils.isEmpty(orderList)) {
            criteriaQuery.orderBy(orderList);
        }
        return repository.findAll(criteriaQuery);
    }


    private List<Order> buildOrder(CriteriaBuilder criteriaBuilder, Root<T> root, String sortFields) {
        if (StringUtil.isNotEmpty(sortFields)) {
            List<Order> orderList = new ArrayList<>();
            List<String> listSort = List.of(sortFields.split(";"));
            for (String s : listSort) {
                if (s.contains(Constants.EQUAL_SIGN)) {
                    String column = s.substring(0, s.indexOf(Constants.EQUAL_SIGN)).trim();
                    String direct = s.substring(s.indexOf(Constants.EQUAL_SIGN) + 2).toLowerCase().trim();
                    if (direct.equals(Constants.SORT_TYPE_ASC)) {
                        orderList.add(criteriaBuilder.asc(root.get(column)));
                    } else {
                        orderList.add(criteriaBuilder.desc(root.get(column)));
                    }
                }
            }
            return orderList;
        }
        return null;
    }

    private List<Sort.Order> buildOrderForPage(String sortFields) {
        if (StringUtil.isNotEmpty(sortFields)) {
            List<String> listSort = List.of(sortFields.split(";"));
            List<Sort.Order> orderList = new ArrayList<>();
            for (String s : listSort) {
                if (s.contains(Constants.EQUAL_SIGN)) {
                    String column = s.substring(0, s.indexOf(Constants.EQUAL_SIGN));
                    String direct = s.substring(s.indexOf(Constants.EQUAL_SIGN ) + 2).toLowerCase();
                    if (direct.equals(Constants.SORT_TYPE_ASC)) {
                        orderList.add(Sort.Order.asc(column));
                    } else {
                        orderList.add(Sort.Order.desc(column));
                    }
                }
            }
            return orderList;
        }
        return null;
    }

    final public List<T> listDistinctRSQL(final SearchRSQLRepository<T> repository,
                                  final BaseSearchRequest searchRequest,
                                  final Class<T> c) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
        Root<T> root = criteriaQuery.from(c);
        if (StringUtil.isNotEmpty(searchRequest.getKeyword())) {
            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<>().defineRoot(root);
            String escapeKeyword = StringUtil.escapeRSQLCharacter(searchRequest.getKeyword());
            Node rootNode = new RSQLParser().parse(escapeKeyword);
            Predicate predicate = rootNode.accept(visitor, entityManager);
            if (ObjectUtil.isNotEmpty(predicate)) {
                criteriaQuery.where(predicate).distinct(true);
            }
        }
        List<Order> orderList = buildOrder(criteriaBuilder, root, searchRequest.getSortField());
        if (!CollectionUtils.isEmpty(orderList)) {
            criteriaQuery.orderBy(orderList);
        }
        return repository.findAll(criteriaQuery);
    }

    final public Page<T> searchDistinctRSQL(final SearchRSQLRepository<T> repository,
                                            final BaseSearchRequest searchRequest,
                                            final Class<T> c,
                                            final String defaultSortColumnName) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
        Root<T> root = criteriaQuery.from(c);
        if (StringUtil.isNotEmpty(searchRequest.getKeyword())) {
            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<>().defineRoot(root);
            String escapeKeyword = StringUtil.escapeRSQLCharacter(searchRequest.getKeyword());
            Node rootNode = new RSQLParser().parse(escapeKeyword);
            Predicate predicate = rootNode.accept(visitor, entityManager);
            criteriaQuery.where(predicate).distinct(true);
        }
        List<Sort.Order> orderList = buildOrderForPage(searchRequest.getSortField());
        Pageable pageable = searchRequest.createPageRequest(defaultSortColumnName, orderList);
        return repository.findByQuery(criteriaQuery, root, pageable);
    }
}