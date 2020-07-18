package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstHinmokuReponse;
import sskf.service.MstHinmokuService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/mst-hinmoku")
@Slf4j
public class MstHinmokuController {

    @Autowired
    private MstHinmokuService mstHinmokuService;

    @GetMapping
    public ResponseEntity<?> listPage(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin API: /mst-himoku listPage");
            Page<MstHinmokuReponse> responses = mstHinmokuService.listPage(searchRequest);
            return ResponseEntity.ok(responses);

        } finally {
            log.info("Log end API: /mst-himoku listPage");
        }

    }

    @GetMapping(value = "/map")
    public ResponseEntity<?> listMap(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin API: /mst-himoku list");
            HashMap<String, MstHinmokuReponse> responses = mstHinmokuService.listMap(searchRequest);
            return ResponseEntity.ok(responses);

        } finally {
            log.info("Log end API: /mst-himoku list");
        }

    }
}
