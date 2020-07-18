package sskf.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class CollectionUtil extends CollectionUtils {

    public static final String COMAN_SPLIT = ",";

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection
     *            the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }


    public static boolean isNotSequence(List<String> list) {
        return !isSequence(list);
    }

    public static boolean isSequence(List<String> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            String current = list.get(i);
            String next = list.get(i + 1);
            if (StringUtil.isEmpty(current) && StringUtil.isNotEmpty(next)) {
                return false;
            }
        }

        return true;
    }

}
