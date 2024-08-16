package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.services.voucher.VoucherServiceImpl;
import org.springframework.stereotype.Component;


@Component
public class VoucherMapper {

    public Voucher toEntity(VoucherDTO dto) {
        if (dto == null) {
            return null;
        }

        Voucher voucher = new Voucher();
        voucher.setId(dto.getId());
        VoucherServiceImpl.updateVoucherFromVoucherDTO(dto, voucher);
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
        dto.setActive(voucher.isActive());
        dto.setPublic(voucher.isPublic());
        dto.setMaximumDiscountValue(voucher.getMaximumDiscountValue());
        dto.setName(voucher.getName());
//        dto.setShopId(voucher.getShopId());
        dto.setStartAt(voucher.getStartAt());
        dto.setQuantity(voucher.getQuantity());
        dto.setMinimumQuantityNeeded(voucher.getMinimumQuantityNeeded());
        dto.setTypeRepeat(voucher.isTypeRepeat());
        // Ignore createdAt and modifiedAt as per the requirement
        return dto;
    }
}
