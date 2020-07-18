package sskf.model.basemodel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sskf.common.CommonFunction;
import sskf.util.CollectionUtil;

import java.util.List;

@Getter
@Setter
public class BaseListRequest {

    protected String sortField;
    protected Integer pageNumber;
    protected Integer limitNumber;

    public PageRequest createPageRequest(String defaultSortFieldName, List<Sort.Order> orderList) {
        // modify the page info

        Sort sort = Sort.by(defaultSortFieldName);
        if (CollectionUtil.isNotEmpty(orderList)) {
            sort = Sort.by(orderList);
        }
        Integer returnPageNumber = CommonFunction.getPageNumber(pageNumber);
        Integer returnLimitNumber = CommonFunction.getLimitNumber(limitNumber);
        return PageRequest.of(returnPageNumber, returnLimitNumber, sort);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("sortField", sortField)
                .append("pageNumber", pageNumber)
                .append("limitNumber", limitNumber)
                .toString();
    }
}
