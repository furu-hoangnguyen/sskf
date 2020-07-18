package sskf.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class CommonFunction {

    private final static Logger logger = LoggerFactory.getLogger(CommonFunction.class);

    public static Sort.Direction getSortType(String sortType) {
        if (sortType == null || sortType.trim().toLowerCase().equals(Constants.SORT_TYPE_DESC)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public static Integer getPageNumber(Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = Constants.START_PAGE_NUMBER;
        }
        return pageNumber;
    }

    public static Integer getLimitNumber(Integer limitNumber) {
        if (limitNumber == null || limitNumber == 0) {
            limitNumber = Constants.NUMBER_ITEMS_PER_PAGE;
        }
        return limitNumber;
    }

    public static boolean checkLength(String inputString, Integer minNumber, Integer maxNumber) {
        if (inputString != null && !inputString.isEmpty()) {
            if (inputString.length() > maxNumber || inputString.length() < minNumber) {
                return false;
            }
        }
        return true;
    }


    public static String getResourceSql(String path) {
        try {
            return StreamUtils.copyToString(new ClassPathResource("sql/" + path).getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {

            logger.error("Error get resource SQL file", e);
            return "";
        }

    }
}
