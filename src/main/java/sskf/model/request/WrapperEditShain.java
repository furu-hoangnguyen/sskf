package sskf.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sskf.model.response.ShainResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrapperEditShain {

    private ShainResponse shainResponse;

    // List delete in table mst_rel_yakushoku_shain
    private List<Integer> yakushokuCds;

}
