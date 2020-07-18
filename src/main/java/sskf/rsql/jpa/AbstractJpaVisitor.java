package sskf.rsql.jpa;

import sskf.rsql.builder.BuilderTools;
import sskf.rsql.builder.SimpleBuilderTools;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import javax.persistence.EntityManager;

/**
 * AbstractQueryVisitor
 *
 * Abstract Visitor class for parsing RSQL AST Nodes.
 *
 * @author AntonioRabelo
 *
 * @param <T> Result type
 * @param <E> Entity type
 */
public abstract class AbstractJpaVisitor<T, E> implements RSQLVisitor<T, EntityManager> {
    protected Class<E> entityClass;

    protected BuilderTools builderTools;

    /**
     * Construtor with template varargs for entityClass discovery.
     *
     * @param e not for usage
     */
    public AbstractJpaVisitor(E... e) {
        // getting class from template... :P
        if (e.length == 0) {
            entityClass = (Class<E>)e.getClass().getComponentType();
        } else {
            entityClass = (Class<E>) e[0].getClass();
        }
    }

    /**
     * Set the entity class explicitly, needed when the entity type is itself a generic
     *
     * @param clazz Class to set.
     */
    public void setEntityClass(Class<E> clazz) {
        entityClass = clazz;
    }

    /**
     * Get builder tools.
     *
     * @return BuilderTools.
     */
    public BuilderTools getBuilderTools() {
        if (this.builderTools == null) {
            this.builderTools = new SimpleBuilderTools();
        }
        return this.builderTools;
    }

    /**
     * Set a predicate strategy.
     *
     * @param delegate PredicateBuilderStrategy.
     */
    public void setBuilderTools(BuilderTools delegate) {
        this.builderTools = delegate;
    }
}
