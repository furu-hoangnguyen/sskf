package sskf.rsql.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
/**
 * Simple implementation of the {@link Mapper}.
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class SimpleMapper implements Mapper {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleMapper.class);

    private Map<Class<?>, Map<String, String>> mapping;


    /**
     * Construct new <tt>SimpleMapper</tt> with zero initial capacity of the
     * entities map.
     */
    public SimpleMapper() {
        this(0);
    }
    /**
     * Construct new <tt>SimpleMapper</tt> with the specified initial capacity
     * of the entities map.
     *
     * @param initialCapacity initial capacity of entities map
     */
    public SimpleMapper(int initialCapacity) {
        mapping = new HashMap<Class<?>, Map<String, String>>(initialCapacity);
    }

    public String translate(String selector, Class<?> entityClass) {
        if (mapping.isEmpty()) return selector;

        Map<String, String> map = mapping.get(entityClass);
        String property = (map != null) ? map.get(selector) : null;

        if (property != null) {
            LOG.debug("Found mapping {} -> {}" , new Object[] {selector, property});
            return property;
        }

        return selector;
    }


    /**
     * Add selectors -> property names mapping for given entity class.
     *
     * @param entityClass entity class
     * @param mapping mapping of selectors to property names
     */
    public void addMapping(Class<?> entityClass, Map<String, String> mapping) {
        this.mapping.put(entityClass, mapping);
    }

    /**
     * Add one selector -> property name mapping for given entity class.
     *
     * @param entityClass entity class
     * @param selector Selector that identifies some element of an entry's content.
     * @param property Name of corresponding entity's property.
     */
    public void addMapping(Class<?> entityClass, String selector, String property) {
        mapping.get(entityClass).put(selector, property);
    }

    /**
     * @see SimpleMapper#setMapping(Map)
     * @return The current mapping of all entities.
     */
    public Map<Class<?>, Map<String, String>> getMapping() {
        return mapping;
    }

    /**
     * Set the mapping of selectors to property names per entity class.
     *
     * @param mapping the mapping
     */
    public void setMapping(Map<Class<?>, Map<String, String>> mapping) {
        this.mapping = mapping;
    }
}
