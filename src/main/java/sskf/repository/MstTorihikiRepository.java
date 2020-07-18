package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sskf.model.entity.MstTorihikiEntity;

import java.util.List;

@Repository
public interface MstTorihikiRepository extends JpaRepository<MstTorihikiEntity, String>, SearchRSQLRepository<MstTorihikiEntity> {

    @Query(value = "SELECT DISTINCT(mst_torihiki.torihiki_rknm_for_search) FROM mst_torihiki mst_torihiki", nativeQuery = true)
    List<String> listTorihikiRnmForSearch();

}
