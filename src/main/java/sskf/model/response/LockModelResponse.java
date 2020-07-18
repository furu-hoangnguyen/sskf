package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LockModelResponse {

    private String editShainCd;

    private String editShainName;

    private Long startedEditAt;

    private Boolean isUpdated;
}
