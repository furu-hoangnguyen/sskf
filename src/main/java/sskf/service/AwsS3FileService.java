package sskf.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import sskf.model.FileModel;

import java.util.List;

public interface AwsS3FileService {
    List<FileModel> uploadFile(List<MultipartFile> uploadingFiles, String folder);

    S3Object fetchObject(String awsFilePath);

    void deleteObject(String awsFilePath);
}
