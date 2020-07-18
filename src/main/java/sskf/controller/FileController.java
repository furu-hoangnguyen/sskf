package sskf.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.service.RequestService;
import sskf.service.impl.AwsS3FileServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    @Autowired
    private AwsS3FileServiceImpl awsS3FileService;

    @Autowired
    private RequestService requestService;

    @GetMapping
    public void ResponseEntity(String awsFilePath, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        S3Object s3Object = awsS3FileService.fetchObject(awsFilePath);
        response.setHeader("Content-Disposition", "attachment;");
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

    }

    @DeleteMapping("/{cd}")
    public ResponseEntity<?> delete(@PathVariable Long cd) {
        requestService.deleteRequestAttachment(cd);
        return ResponseEntity.ok("Delete file success!");
    }
}
