package sskf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sskf.model.entity.ShainAddressEntity;

import java.util.List;

@Repository
public interface ShainAddressRepository extends JpaRepository<ShainAddressEntity, String>, SearchRSQLRepository<ShainAddressEntity> {

    @Query(value = "select address.mail_address from shain_addresses address INNER JOIN shain shain on address.shain_cd = shain.shain_cd and shain.bumon_cd = '4000'", nativeQuery = true)
    List<String> listEmailAccountingCharge();
}
