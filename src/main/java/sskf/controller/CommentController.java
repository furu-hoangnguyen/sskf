package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.basemodel.BaseTimeModel;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;
import sskf.service.CommentsForDetailsService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {

    @Autowired
    private CommentsForDetailsService commentsForDetailsService;

    @GetMapping("/old-comment")
    public ResponseEntity<?> listOldComment(String storeGCd) {
        try {
            log.info("Log begin API: /comments listOldComment");
            List<Object> responses = commentsForDetailsService.listOldComment(storeGCd);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /comments listOldComment");
        }

    }

    @GetMapping
    public ResponseEntity<?> listByRequestAccount(Long requestAccountId) {
        try {
            log.info("Log begin API: /comments list");
            List<CommentDetailResponse> responses = commentsForDetailsService.listByRequestAccount(requestAccountId);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /comments list");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            log.info("Log begin API: /comments listOldComment");
            commentsForDetailsService.delete(id);
            return ResponseEntity.ok("Deleted success");
        } finally {
            log.info("Log end API: /comments listOldComment");
        }

    }

    @GetMapping("/list")
    public ResponseEntity<?> list(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Log begin API: /comments list");
            List<CommentDetailResponse> responses = commentsForDetailsService.list(baseSearchRequest);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /comments list");
        }

    }

    @PostMapping("/for-item/{requestCd}")
    public ResponseEntity<?> insertForItem(@PathVariable Long requestCd, @RequestBody List<CommentDetailsRequest> commentDetailsRequest) {
        try {
            log.info("Log begin API: /comments/for-item");
            BaseTimeModel responses = commentsForDetailsService.insertForItem(requestCd, commentDetailsRequest);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: /comments/for-item");
        }

    }
}

