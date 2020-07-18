package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sskf.common.Constants;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.ShainResponse;
import sskf.service.ShainService;

import java.util.List;

@RequestMapping("/api/shain")
@RestController
@Slf4j
public class ShainController {

    @Autowired
    private ShainService shainService;

    @GetMapping("/information")
    public ResponseEntity<ShainResponse> getInformation() {
        ShainResponse shainResponse = shainService.getInformation();
        return ResponseEntity.ok(shainResponse);
    }

    @GetMapping("/get-shains")
    public ResponseEntity<?> listShains(BaseSearchRequest baseSearchRequest) {
        Page<ShainResponse> shainResponse = shainService.listShains(baseSearchRequest);

        return ResponseEntity.ok(shainResponse);
    }

    @PreAuthorize("hasAuthority('" + Constants.ROLE_ADMIN + "')")
    @GetMapping("/get-shains-master")
    public ResponseEntity<?> listShainsMaster(BaseSearchRequest baseSearchRequest) {
        Page<ShainResponse> shainResponse = shainService.listShainsMaster(baseSearchRequest);

        return ResponseEntity.ok(shainResponse);
    }

    @GetMapping("/get-shain-for-approval")
    public ResponseEntity<?> listShainForApproval(@RequestParam Byte stepNumber, @RequestParam String bumonCd, @RequestParam Long requestCd) {
        try {
            log.info("Log begin API: shain listShainForApproval");
            List<ShainResponse> responses = shainService.listShainForApproval(stepNumber, bumonCd, requestCd);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: shain listShainForApproval");
        }

    }

    @PostMapping("/update")
    public ResponseEntity<ShainResponse> updateShainInformation(@RequestBody ShainResponse shainResponse) {
        ShainResponse shainResponseUpdated = shainService.updateShainInformation(shainResponse);

        return ResponseEntity.ok(shainResponseUpdated);
    }

}
