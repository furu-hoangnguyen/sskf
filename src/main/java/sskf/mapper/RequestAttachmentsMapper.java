package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sskf.model.FileModel;
import sskf.model.entity.RequestAttachmentsEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestAttachmentsMapper {

    @Mapping(source = "fileOriginalName", target = "displayedFileName")
    @Mapping(source = "awsS3FilePath", target = "attachmentPath")
    public abstract RequestAttachmentsEntity toEntity(FileModel fileModel);

    @Mapping(source = "displayedFileName", target = "fileOriginalName")
    @Mapping(source = "attachmentPath", target = "awsS3FilePath")
    @Mapping(source = "requestAttachmentCd", target = "requestAttachmentCd")
    public abstract FileModel toFileModel(RequestAttachmentsEntity entity);
}
