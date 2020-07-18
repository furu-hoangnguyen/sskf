package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MstHinmokuReponse {

    private String hinmokuCd;

    private String hinmokuCdNk;

    private String brandKbn;

    private String hanKeitaiKbn;

    private String hinmokuKbn;

    private String jigyoCd;

    private String hinmokuKnm;

    private String hinmokuRnm;

    private String hinmokuKnmForSearch;

    private String hinmokuRnmForSearch;

    private Integer kikaku;

    private String nisugata;

    private String nisugataForSearch;

    private Integer irisu;

    private String yoryoTani;

    private BigDecimal mfYen;

    private BigDecimal hyojyunYen;

    private BigDecimal mishuLimit;

    private String categoryHinKbn;

    private String categoryHinsubKbn;

    private String categorySeriesKbn;

    private LocalDateTime batchUpdateDateHinmoku;

    private LocalDateTime updateDateAw;

    private LocalDateTime updateDateMb;

}
