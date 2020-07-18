package sskf.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrapperRequest {

    // 3: Request list - In progress, 4: Request list - All, 5: Request list - Send back, 6: Request list - Reject
    // 7: Request list - Create, 8: Request list - Confirm, 9: Request list - Apply, 10: Request list - Approve
    // 11: Request list - Confirm settlement
    private int type;

    private int page;

    private String field;

    private String orderBy;

    private int limit;

    private FilterRequest filterRequest;

}
