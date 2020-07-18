package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.FileModel;
import sskf.model.entity.RequestAttachmentsEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RequestAttachmentsMapperImpl extends RequestAttachmentsMapper {

    @Override
    public RequestAttachmentsEntity toEntity(FileModel fileModel) {
        if ( fileModel == null ) {
            return null;
        }

        RequestAttachmentsEntity requestAttachmentsEntity = new RequestAttachmentsEntity();

        requestAttachmentsEntity.setAttachmentPath( fileModel.getAwsS3FilePath() );
        requestAttachmentsEntity.setDisplayedFileName( fileModel.getFileOriginalName() );
        requestAttachmentsEntity.setRequestAttachmentCd( fileModel.getRequestAttachmentCd() );

        return requestAttachmentsEntity;
    }

    @Override
    public FileModel toFileModel(RequestAttachmentsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        FileModel fileModel = new FileModel();

        fileModel.setAwsS3FilePath( entity.getAttachmentPath() );
        fileModel.setRequestAttachmentCd( entity.getRequestAttachmentCd() );
        fileModel.setFileOriginalName( entity.getDisplayedFileName() );

        return fileModel;
    }
}
