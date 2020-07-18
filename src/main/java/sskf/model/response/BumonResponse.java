package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@NoArgsConstructor
@Getter
@Setter
public class BumonResponse {
    private String bumonCd;

    private String bumonCdNk;

    private String bumonNm;

    private String bumonKnm;

    private String bumonRnm;
}
