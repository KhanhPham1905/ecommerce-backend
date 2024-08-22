package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findByShopId(Long shopId);
    List<Voucher> findByShopIdAndIsActiveAndIsPublic(Long shopId, boolean isActive, boolean isPublic);

    @Query("SELECT COUNT(*) FROM Voucher v WHERE v.shopId = ?1")
    Long getQuantityByShopId(Long shopId);
    List<Voucher> findAllByIsActiveTrueAndIsPublicFalse();
    List<Voucher> findAllByIsActiveTrue();
    // For the better version, not being used for now
    List<Voucher> findAllByIsActiveTrueAndExpiredAtAfter(LocalDateTime now);

    // Something
    @Modifying
    @Query("UPDATE Voucher v SET v.discountType = 'NONE' WHERE v.discountType IS NULL")
    void updateDiscountTypeToNone();
}
