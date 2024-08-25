package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.models.enums.DiscountType;
import com.ghtk.ecommercewebsite.models.enums.RepeatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IVoucherService {
    Page<VoucherDTO> findAllVouchersByShopFromUser(Long userId, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findAllVouchersByShopFromSeller(Long userId, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findVouchersByNameFromSeller(Long userId, String name, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findVouchersByCouponCodeFromSeller(Long userId, String couponCode, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findVouchersByDiscountTypeFromSeller(Long userId, DiscountType discountType, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findVouchersByRepeatTypeFromSeller(Long userId, RepeatType repeatType, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findActiveVouchersFromSeller(Long userId, Boolean isActive, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findPublicVouchersFromSeller(Long userId, Boolean isPublic, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> searchVouchersByStartDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> searchVouchersByExpiredDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) throws DataNotFoundException;
    Page<VoucherDTO> findActiveAndPublicVouchersFromSeller(Long userId, Boolean isActive, Boolean isPublic, Pageable pageable) throws DataNotFoundException;
//    Page<VoucherDTO> findVouchersByIsActiveFromSeller(Long userId, Boolean isActive, Pageable pageable) throws DataNotFoundException;
    List<Voucher> findAll();

    Optional<Voucher> findById(Long id);

    Voucher save(Voucher voucher);

    void deleteById(Long id);

    List<Voucher> findByShopId(Long shopId) ;

    VoucherDTO createNewVoucher(VoucherDTO voucherDTO, Long userId) throws DataNotFoundException;

    VoucherDTO getVoucherByIdByShopFromUser(Long voucherId, Long userId) throws DataNotFoundException;

    VoucherDTO updateVoucherByIdByShopFromUser(Long voucherId, Long userId, VoucherDTO voucherDTO) throws DataNotFoundException;

    void deleteVoucherByIdByShopFromUser(Long voucherId, Long userId) throws DataNotFoundException;

    VoucherDTO setVoucherActive(Long voucherId, Long userId) throws DataNotFoundException;

    VoucherDTO setVoucherInactive(Long voucherId, Long userId) throws DataNotFoundException;

    VoucherDTO setVoucherPublic(Long voucherId, Long userId) throws DataNotFoundException;

    VoucherDTO setVoucherPrivate(Long voucherId, Long userId) throws DataNotFoundException;

    List<VoucherDTO> findAllVouchersByShopId(Long shopId);

    VoucherDTO switchVoucherStatus(Long voucherId, Long userId) throws DataNotFoundException;
}
