package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstRequestCommentActionsResponse;
import sskf.model.response.OperationHistoriesOfMstCommentResponse;
import sskf.service.MstRequestCommentActionsService;

import java.util.List;

@RestController
@RequestMapping("/api/mst-request-comment-actions")
@Slf4j
public class MstRequestCommentActionsController {

    @Autowired
    private MstRequestCommentActionsService mstRequestCommentActionsService;

    @GetMapping("/{requestCd}")
    public ResponseEntity<?> list(@PathVariable Long requestCd) {
        try {
            log.info("Log begin MstRequestCommentActionsController: list");
            List<OperationHistoriesOfMstCommentResponse> responses = mstRequestCommentActionsService.list(requestCd);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end MstRequestCommentActionsController: list");
        }

    }
}
