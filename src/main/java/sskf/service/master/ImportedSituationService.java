package sskf.service.master;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ImportedMasterStatusEntity;
import sskf.model.response.ImportedMasterStatusResponse;
import sskf.repository.ImportedMasterStatusRepository;
import sskf.service.impl.BaseServiceHasSearchRSQL;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static sskf.specs.ImportedMasterStatusSpecification.hasCreatedAt;
import static sskf.specs.ImportedMasterStatusSpecification.hasImportedTableName;
import static sskf.specs.ImportedMasterStatusSpecification.hasNumber;

@Slf4j
@Service
public class ImportedSituationService extends BaseServiceHasSearchRSQL<ImportedMasterStatusEntity> {
    @Autowired
    private ImportedMasterStatusRepository importedMasterStatusRepository;

    public Page<ImportedMasterStatusEntity> list(BaseSearchRequest request) {
        Page<ImportedMasterStatusEntity> result = searchRSQL(importedMasterStatusRepository,
                request, ImportedMasterStatusEntity.class, "cd");
        return result;
    }

    public List<ImportedMasterStatusResponse> getRequestStatus(String importedTableName, String number, String fromCreatedAt, String toCreatedAt) {

        try {
            log.info("Begin service ImportedSituationService: getRequestStatus");

            Date fromCreatedAtDate = null;
            Date toCreatedAtDate = null;
            Byte numberByte = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());

            if (!StringUtils.isEmpty(fromCreatedAt)) {
                fromCreatedAt += " 00:00:00";
                fromCreatedAtDate = simpleDateFormat.parse(fromCreatedAt);
            }

            if (!StringUtils.isEmpty(toCreatedAt)) {
                toCreatedAt += " 23:59:59";
                toCreatedAtDate = simpleDateFormat.parse(toCreatedAt);
            }

            if (!StringUtils.isEmpty(number)) {
                numberByte = Byte.valueOf(number);
            }

            Specification<ImportedMasterStatusEntity> specification = Specification.where(hasImportedTableName(importedTableName))
                    .and(hasNumber(numberByte)).and(hasCreatedAt(fromCreatedAtDate, toCreatedAtDate));
            Pageable pageable = PageRequest.of(0, 120);

            Page<ImportedMasterStatusEntity> importedMasterStatusEntities = importedMasterStatusRepository.findAll(specification, pageable);

            List<ImportedMasterStatusResponse> importedMasterStatusResponses = importedMasterStatusEntities.getContent().stream()
                    .map(importedMasterStatusEntity -> {

                        ImportedMasterStatusResponse importedMasterStatusResponse = new ImportedMasterStatusResponse();
                        importedMasterStatusResponse.setCd(importedMasterStatusEntity.getCd());
                        importedMasterStatusResponse.setImportedTableName(importedMasterStatusEntity.getImportedTableName());
                        importedMasterStatusResponse.setNumber(importedMasterStatusEntity.getNumber());

                        Date importedAt = Date.from(importedMasterStatusEntity.getImportedAt().atZone(ZoneId.systemDefault()).toInstant());
                        importedMasterStatusResponse.setImportedAt(importedAt);

                        Date createdAt = Date.from(importedMasterStatusEntity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
                        importedMasterStatusResponse.setCreatedAt(createdAt);

                        return importedMasterStatusResponse;
                    }).collect(Collectors.toList());

             return importedMasterStatusResponses;
        } catch (Exception e) {
            throw new RuntimeException("無効なリクエストです。リクエスト内容を確認してください。");
        } finally {
            log.info("End service ImportedSituationService: getRequestStatus");
        }
    }
}
