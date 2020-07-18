package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MstRequestCommentActionsResponse {
    private Long cd;
    private String name;
    List<OperationHistoriesOfMstCommentResponse> operationHistoriesResponses;
}
