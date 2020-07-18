package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstTorihikiRespone;
import sskf.service.MstTorihikiService;

import java.util.List;

@RestController
@RequestMapping("/api/mst-torihiki")
@Slf4j
public class MstTorihikiController {

    @Autowired
    private MstTorihikiService mstTorihikiService;

    @GetMapping("/torihiki-rnm-for-search-dropdown")
    public ResponseEntity<?> listTorihikiRnmForSearch(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin API: /torihiki-rnm-for-search-dropdown");
            List<String> responses = mstTorihikiService.listTorihikiRnmForSearch(searchRequest);
            return ResponseEntity.ok(responses);

        } finally {
            log.info("Log end API: /torihiki-rnm-for-search-dropdown");
        }

    }

    @GetMapping("/torihikis")
    public ResponseEntity<?> listTorihiki(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin API: /torihikis");
            Page<MstTorihikiRespone> responses = mstTorihikiService.listTorihiki(searchRequest);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /torihikis");
        }

    }

}
