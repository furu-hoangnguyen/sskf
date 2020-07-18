package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MstTantoResponse {

    private String tantoCd;

    private String tantoStatus;

    private LocalDateTime batchUpdateDate;

    private String bumonNm;

    private String shainNm;

}
