package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MstTorihikiRespone {

    private String torihikiCd;

    private String torihikiCdNk;

    private String torihiki1Nm;

    private String torihiki2Nm;

    private String torihikiRnm;

    private String torihikiRknm;

    private String seikyuFlg;

    private String choaiKoriFlg;

    private String torihikiStatus;

    private LocalDateTime batchUpdateDate;

    private String torihikiRnmForSearch;

    private String torihikiRknmForSearch;
}
