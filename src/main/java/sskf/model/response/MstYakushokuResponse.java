package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MstYakushokuResponse {

    private String cd;

    private Byte isDeleted;

    private String name;

}
