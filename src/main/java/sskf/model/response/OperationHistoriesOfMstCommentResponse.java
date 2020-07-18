package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OperationHistoriesOfMstCommentResponse {
    private Long cd;
    private String bumonNm;
    private String shainNm;
    private LocalDateTime createdAt;
    private String comment;
    private Boolean isDeputy;
    private String actionComment;
    private Byte stepNumber;
    private List<OperationHistoriesOfMstCommentResponse> historiesOfMstCommentChild;
}
