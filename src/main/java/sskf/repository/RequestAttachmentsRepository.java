package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sskf.model.entity.RequestAttachmentsEntity;

import java.util.List;

public interface RequestAttachmentsRepository extends JpaRepository<RequestAttachmentsEntity, Long> {

    List<RequestAttachmentsEntity> findAllByDisplayedFileNameIn(List<String> filesName);
}
