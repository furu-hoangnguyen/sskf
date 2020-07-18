package sskf.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sskf.exception.SSKFException;
import sskf.model.FileModel;
import sskf.service.AwsS3FileService;
import sskf.util.CollectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@PropertySource({"classpath:application.properties"})
public class AwsS3FileServiceImpl implements AwsS3FileService {
    private AmazonS3 amazonS3Client;

    @Autowired
    private Environment env;

    @Autowired
    public AwsS3FileServiceImpl(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public List<FileModel> uploadFile(List<MultipartFile> uploadingFiles, String folder) {
        try {
            log.info("Log begin uploadFile to S3");
            List<FileModel> uploadedFiles = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(uploadingFiles)) {
                for (MultipartFile uploadingFile : uploadingFiles) {
                    String awsS3UniqueFilePath = folder + "/" + uploadingFile.getOriginalFilename();
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentLength(uploadingFile.getSize());

                    this.amazonS3Client.putObject(env.getProperty("aws.s3.bucket.name"), awsS3UniqueFilePath, uploadingFile.getInputStream(), objectMetadata);

                    uploadedFiles.add(new FileModel(uploadingFile.getOriginalFilename(), awsS3UniqueFilePath, uploadingFile.getSize()));
                }
            }
            return uploadedFiles;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new SSKFException("Upload file error", e.getMessage());
        } finally {
            log.info("Log end uploadFile to S3");
        }
    }

    @Override
    public S3Object fetchObject(String awsFilePath) {
        return this.amazonS3Client.getObject(new GetObjectRequest(env.getProperty("aws.s3.bucket.name"), awsFilePath));
    }

    @Override
    public void deleteObject(String awsFilePath) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(env.getProperty("aws.s3.bucket.name"), awsFilePath));
    }
}
