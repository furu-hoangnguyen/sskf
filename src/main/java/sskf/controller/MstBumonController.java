package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.response.BumonResponse;
import sskf.service.MstBumonService;

import java.util.List;

@RestController
@RequestMapping("/api/mst-bumon")
@Slf4j
public class MstBumonController {

    @Autowired
    private MstBumonService mstBumonService;

    @GetMapping
    public ResponseEntity<?> listDepartmentByStepNumber(@RequestParam Byte stepNumber) {
        try {
            log.info("Log begin API: /mst-bumon listDepartmentByStepNumber");
            List<BumonResponse > responses = mstBumonService.listDepartmentByStepNumber(stepNumber);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /mst-bumon listDepartmentByStepNumber");
        }

    }
}
