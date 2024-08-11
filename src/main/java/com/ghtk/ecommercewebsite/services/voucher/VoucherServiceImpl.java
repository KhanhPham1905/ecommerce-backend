package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.mapper.VoucherMapper;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements IVoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Override
    public Optional<Voucher> findById(Long id) {
        return voucherRepository.findById(id);
    }

    @Override
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public void deleteById(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public List<Voucher> findByShopId(Long shopId) {
        return voucherRepository.findByShopId(shopId);
    }

    // Các phương thức mới

    @Override
    public List<VoucherDTO> getAllVouchers() {
        return findAll().stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherDTO getVoucherById(Long id) {
        return findById(id)
                .map(voucherMapper::toDTO)
                .orElse(null); // Xử lý null khi không tìm thấy
    }

    @Override
    public VoucherDTO createVoucher(VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.toEntity(voucherDTO);
        Voucher savedVoucher = save(voucher);
        return voucherMapper.toDTO(savedVoucher);
    }

    @Override
    public VoucherDTO updateVoucher(Long id, VoucherDTO voucherDetails) {
        return findById(id)
                .map(voucher -> {
                    voucher.setCouponCode(voucherDetails.getCouponCode());
                    voucher.setDiscountType(voucherDetails.getDiscountType());
                    voucher.setDiscountValue(voucherDetails.getDiscountValue());
                    voucher.setExpiredAt(voucherDetails.getExpiredAt());
                    voucher.setIsActive(voucherDetails.getIsActive());
                    voucher.setIsPublic(voucherDetails.getIsPublic());
                    voucher.setName(voucherDetails.getName());
                    voucher.setShopId(voucherDetails.getShopId());
                    voucher.setStartAt(voucherDetails.getStartAt());
                    voucher.setQuantity(voucherDetails.getQuantity());
                    voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
                    voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
                    Voucher updatedVoucher = save(voucher);
                    return voucherMapper.toDTO(updatedVoucher);
                }).orElse(null);
    }

    @Override
    public VoucherDTO patchVoucher(Long id, VoucherDTO voucherDetails) {
        Optional<Voucher> optionalVoucher = findById(id);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();
            if (voucherDetails.getCouponCode() != null) voucher.setCouponCode(voucherDetails.getCouponCode());
            if (voucherDetails.getDiscountType() != 0) voucher.setDiscountType(voucherDetails.getDiscountType());
            if (voucherDetails.getDiscountValue() != null) voucher.setDiscountValue(voucherDetails.getDiscountValue());
            if (voucherDetails.getExpiredAt() != null) voucher.setExpiredAt(voucherDetails.getExpiredAt());
            if (voucherDetails.getIsActive() != voucher.getIsActive()) voucher.setIsPublic(voucherDetails.getIsActive());
            if (voucherDetails.getIsActive() != voucher.getIsPublic()) voucher.setIsActive(voucherDetails.getIsPublic());
            if (voucherDetails.getName() != null) voucher.setName(voucherDetails.getName());
            if (voucherDetails.getShopId() != null) voucher.setShopId(voucherDetails.getShopId());
            if (voucherDetails.getStartAt() != null) voucher.setStartAt(voucherDetails.getStartAt());
            if (voucherDetails.getQuantity() != 0) voucher.setQuantity(voucherDetails.getQuantity());
            if (voucherDetails.getMinimumQuantityNeeded() != 0)
                voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
            if (voucherDetails.isTypeRepeat() != voucher.isTypeRepeat())
                voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
            Voucher updatedVoucher = save(voucher);
            return voucherMapper.toDTO(updatedVoucher);
        } else {
            return null;
        }
    }
    @Override
    public void softDeleteVoucher(Long id) {
        findById(id).ifPresent(voucher -> {
            voucher.setIsActive(false);
            save(voucher);
        });
    }
    @Override
    public List<VoucherDTO> getVouchersByShopId(Long shopId) {
        return findByShopId(shopId).stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }
}
