package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import org.springframework.stereotype.Component;


@Component
public class VoucherMapper {

    public Voucher toEntity(VoucherDTO dto) {
        if (dto == null) {
            return null;
        }

        Voucher voucher = new Voucher();
        voucher.setId(dto.getId());
        voucher.setCouponCode(dto.getCouponCode());
        voucher.setDiscountType(dto.getDiscountType());
        voucher.setDiscountValue(dto.getDiscountValue());
        voucher.setExpiredAt(dto.getExpiredAt());
        voucher.setIsPublic(dto.getIsActive());
        voucher.setIsPublic(dto.getIsPublic());
        voucher.setMaximumDiscountValue(dto.getMaximumDiscountValue());
        voucher.setName(dto.getName());
        voucher.setShopId(dto.getShopId());
        voucher.setStartAt(dto.getStartAt());
        voucher.setQuantity(dto.getQuantity());
        voucher.setMinimumQuantityNeeded(dto.getMinimumQuantityNeeded());
        voucher.setTypeRepeat(dto.isTypeRepeat());
        voucher.setStartAt(dto.getStartAt());
        // Ignore createdAt and modifiedAt as per the requirement
        return voucher;
    }

    public VoucherDTO toDTO(Voucher voucher) {
        if (voucher == null) {
            return null;
        }

        VoucherDTO dto = new VoucherDTO();
        dto.setId(voucher.getId());
        dto.setCouponCode(voucher.getCouponCode());
        dto.setDiscountType(voucher.getDiscountType());
        dto.setDiscountValue(voucher.getDiscountValue());
        dto.setExpiredAt(voucher.getExpiredAt());
        dto.setIsActive(voucher.getIsActive());
        dto.setIsPublic(voucher.getIsPublic());
        dto.setMaximumDiscountValue(voucher.getMaximumDiscountValue());
        dto.setName(voucher.getName());
        dto.setShopId(voucher.getShopId());
        dto.setStartAt(voucher.getStartAt());
        dto.setQuantity(voucher.getQuantity());
        dto.setMinimumQuantityNeeded(voucher.getMinimumQuantityNeeded());
        dto.setTypeRepeat(voucher.isTypeRepeat());
        dto.setStartAt(voucher.getStartAt());
        // Ignore createdAt and modifiedAt as per the requirement
        return dto;
    }
}
