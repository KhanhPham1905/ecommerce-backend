package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.voucher.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final IVoucherService iVoucherService;

    @GetMapping
    public CommonResult<List<VoucherDTO>> getAllVoucher() {
        List<VoucherDTO> vouchers = iVoucherService.getAllVouchers();
        return CommonResult.success(vouchers, "Get all vouchers successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<VoucherDTO> getVoucherById(@PathVariable Long id) {
        VoucherDTO voucherDTO = iVoucherService.getVoucherById(id);
        if (voucherDTO != null) {
            return CommonResult.success(voucherDTO, "Get voucher successfully");
        } else {
            return CommonResult.error(404, "Voucher not found");
        }
    }

    @PostMapping
    public CommonResult<VoucherDTO> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherDTO savedVoucherDTO = iVoucherService.createVoucher(voucherDTO);
        return CommonResult.success(savedVoucherDTO, "Create voucher successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<VoucherDTO> updateVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
        VoucherDTO updatedVoucherDTO = iVoucherService.updateVoucher(id, voucherDetails);
        if (updatedVoucherDTO != null) {
            return CommonResult.success(updatedVoucherDTO, "Update voucher successfully");
        } else {
            return CommonResult.error(404, "Voucher not found");
        }
    }

    @PatchMapping("/{id}")
    public CommonResult<VoucherDTO> patchVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
        VoucherDTO patchedVoucherDTO = iVoucherService.patchVoucher(id, voucherDetails);
        if (patchedVoucherDTO != null) {
            return CommonResult.success(patchedVoucherDTO, "Patch voucher successfully");
        } else {
            return CommonResult.error(404, "Voucher not found");
        }
    }

    @DeleteMapping("/{id}")
    public CommonResult<String> deleteVoucher(@PathVariable Long id) {
        iVoucherService.softDeleteVoucher(id);
        return CommonResult.success("Voucher with ID " + id + " has been deleted.");
    }

    @GetMapping("/shop/{shopId}")
    public CommonResult<List<VoucherDTO>> getVouchersByShopId(@PathVariable Long shopId) {
        List<VoucherDTO> voucherDTOs = iVoucherService.getVouchersByShopId(shopId);
        return CommonResult.success(voucherDTOs, "Get vouchers by shop ID successfully");
    }


}
