package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentDetailResponse {

    private Long cd;

    private Byte isDuplicated;

    private Byte isCapableOfBeingDeleted;

    private String shainNm;

    private String bumonNm;

    private Byte isDeputy;

    private String comment;

    private LocalDateTime createdAt;

    private String itemNumber;
}
