package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalPerson {

    public ApprovalPerson(String bumonCd, String bumonNm, String shainCd, String shainNm) {
        this.bumonCd = bumonCd;
        this.bumonNm = bumonNm;
        this.shainCd = shainCd;
        this.shainNm = shainNm;
    }

    private Long cd;

    private Byte stepNumber;

    private Boolean isDeputy;

    private String bumonCd;

    private String bumonNm;

    private String shainNm;

    private String shainCd;
}
