package sskf.util;

import org.springframework.util.ObjectUtils;

public class ObjectUtil extends ObjectUtils {

    /**
     * check not empty two object
     *
     * @param object the object compare
     * @return the result of compare
     */
    public static <T extends Object> Boolean isNotEmpty(T object) {
        return !ObjectUtils.isEmpty(object);
    }
}
