package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findByShopId(Long shopId);
    List<Voucher> findByShopIdAndIsActiveAndIsPublic(Long shopId, boolean isActive, boolean isPublic);

    @Query("SELECT COUNT(*) FROM Voucher v WHERE v.shopId = ?1")
    Long getQuantityByShopId(Long shopId);
}
