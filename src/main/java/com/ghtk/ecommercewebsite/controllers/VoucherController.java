package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.VoucherMapper;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.services.voucher.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/vouchers")
public class VoucherController {

    private final IVoucherService iVoucherService;
    private final VoucherMapper voucherMapper;

    @Autowired
    public VoucherController(IVoucherService iVoucherService, VoucherMapper voucherMapper) {
        this.iVoucherService = iVoucherService;
        this.voucherMapper = voucherMapper;
    }

    @GetMapping
    public List<VoucherDTO> getAllVoucher() {
        return iVoucherService.findAll().stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherDTO> getVoucherById(@PathVariable Long id) {
        return iVoucherService.findById(id).map(
                        voucher -> ResponseEntity.ok(voucherMapper.toDTO(voucher)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VoucherDTO> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.toEntity(voucherDTO);
        Voucher savedVoucher = iVoucherService.save(voucher);
        return ResponseEntity.ok(voucherMapper.toDTO(savedVoucher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherDTO> updateVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
        return iVoucherService.findById(id)
                .map(voucher -> {
                    voucher.setCouponCode(voucherDetails.getCouponCode());
                    voucher.setDiscountType(voucherDetails.getDiscountType());
                    voucher.setDiscountValue(voucherDetails.getDiscountValue());
                    voucher.setExpiredAt(voucherDetails.getExpiredAt());
                    voucher.setActive(voucherDetails.isActive());
                    voucher.setPublic(voucherDetails.isPublic());
                    voucher.setName(voucherDetails.getName());
                    voucher.setShopId(voucherDetails.getShopId());
                    voucher.setStartAt(voucherDetails.getStartAt());
                    voucher.setQuantity(voucherDetails.getQuantity());
                    voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
                    voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
                    Voucher updatedVoucher = iVoucherService.save(voucher);
                    return ResponseEntity.ok(voucherMapper.toDTO(updatedVoucher));
                }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VoucherDTO> patchVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
        Optional<Voucher> optionalVoucher = iVoucherService.findById(id);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();
            if (voucherDetails.getCouponCode() != null) voucher.setCouponCode(voucherDetails.getCouponCode());
            if (voucherDetails.getDiscountType() != 0) voucher.setDiscountType(voucherDetails.getDiscountType());
            if (voucherDetails.getDiscountValue() != null) voucher.setDiscountValue(voucherDetails.getDiscountValue());
            if (voucherDetails.getExpiredAt() != null) voucher.setExpiredAt(voucherDetails.getExpiredAt());
            if (voucherDetails.isActive() != voucher.isActive()) voucher.setActive(voucherDetails.isActive());
            if (voucherDetails.isPublic() != voucher.isPublic()) voucher.setPublic(voucherDetails.isPublic());
            if (voucherDetails.getName() != null) voucher.setName(voucherDetails.getName());
            if (voucherDetails.getShopId() != null) voucher.setShopId(voucherDetails.getShopId());
            if (voucherDetails.getStartAt() != null) voucher.setStartAt(voucherDetails.getStartAt());
            if (voucherDetails.getQuantity() != 0) voucher.setQuantity(voucherDetails.getQuantity());
            if (voucherDetails.getMinimumQuantityNeeded() != 0)
                voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
            if (voucherDetails.isTypeRepeat() != voucher.isTypeRepeat())
                voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
            Voucher updatedVoucher = iVoucherService.save(voucher);
            return ResponseEntity.ok(voucherMapper.toDTO(updatedVoucher));
        } else return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVoucher(@PathVariable Long id) {
        return iVoucherService.findById(id)
                .map(voucher -> {
                    iVoucherService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
