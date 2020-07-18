package sskf.model.basemodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseSearchRequest extends BaseListRequest{

    protected String keyword;

    public BaseSearchRequest() {
        super();
    }
}
