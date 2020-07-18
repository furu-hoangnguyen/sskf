package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MstStoreResponse {

    private String storeCd;

    private String storeGCd;

    private String storeGNm;

    private String shainCd;

    private String shainNm;

    private LocalDateTime updateDateAw;

    private LocalDateTime updateDateMb;
}
