package sskf.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class MstRelYakushokuShainEntityPK implements Serializable {

    @Column(name = "yakusyoku_cd")
    private String yakusyokuCd;

    @Column(name = "shain_cd")
    private String shainCd;

}
