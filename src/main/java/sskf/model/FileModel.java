package sskf.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class FileModel {

    private Long requestAttachmentCd;
    private String fileOriginalName;
    private String awsS3FilePath;
    private Long fileSize;

    public FileModel(String fileOriginalName, String awsS3FilePath, Long fileSize) {
        this.fileOriginalName = fileOriginalName;
        this.awsS3FilePath = awsS3FilePath;
        this.fileSize = fileSize;
    }

}
