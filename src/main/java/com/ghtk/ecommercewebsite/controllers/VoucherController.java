package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.VoucherMapper;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.models.enums.DiscountType;
import com.ghtk.ecommercewebsite.models.enums.RepeatType;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.voucher.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final IVoucherService iVoucherService;
//    private final VoucherMapper voucherMapper;

    // User gets all vouchers from a shop, by shop's id
    @GetMapping("/getVouchers/{shopId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResult<List<VoucherDTO>> getAllVouchersByShopId(@PathVariable Long shopId) {
        return CommonResult.success(iVoucherService.findAllVouchersByShopId(shopId));
    }

    // From here is for sellers
    @GetMapping("/getAllVouchers")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getAllVoucherByShopFromSeller(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findAllVouchersByShopFromSeller(user.getId(), pageable));
    }

    @GetMapping("/search/name")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getVouchersByNameFromSeller(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findVouchersByNameFromSeller(user.getId(), name, pageable));
    }

    @GetMapping("/search/couponCode")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getVouchersByCouponCodeFromSeller(
            @RequestParam("couponCode") String couponCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findVouchersByCouponCodeFromSeller(user.getId(), couponCode, pageable));
    }

    @GetMapping("/search/discountType")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getVouchersByDiscountTypeFromSeller(
            @RequestParam("discountType") DiscountType discountType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findVouchersByDiscountTypeFromSeller(user.getId(), discountType, pageable));
    }

    @GetMapping("/search/repeatType")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getVouchersByRepeatTypeFromSeller(
            @RequestParam("repeatType") RepeatType repeatType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findVouchersByRepeatTypeFromSeller(user.getId(), repeatType, pageable));
    }

    @GetMapping("/search/active")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getActiveVouchersFromSeller(
            @RequestParam("isActive") Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findActiveVouchersFromSeller(user.getId(), isActive, pageable));
    }

    @GetMapping("/search/public")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getPublicVouchersFromSeller(
            @RequestParam("isPublic") Boolean isPublic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findPublicVouchersFromSeller(user.getId(), isPublic, pageable));
    }

    @GetMapping("/search/startDate")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> searchVouchersByStartDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.searchVouchersByStartDateRange(user.getId(), startDate, endDate, pageable));
    }

    @GetMapping("/search/expiryDate")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> searchVouchersByExpiredDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.searchVouchersByExpiredDateRange(user.getId(), startDate, endDate, pageable));
    }

    @GetMapping("/search/activePublic")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<VoucherDTO>> getActiveAndPublicVouchersFromSeller(
            @RequestParam("isActive") Boolean isActive,
            @RequestParam("isPublic") Boolean isPublic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws DataNotFoundException {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.findActiveAndPublicVouchersFromSeller(user.getId(), isActive, isPublic, pageable));
    }

    @PostMapping("/createNewVoucher")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> createNewVoucher(@RequestBody VoucherDTO voucherDTO) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.createNewVoucher(voucherDTO, user.getId()));
    }

    @GetMapping("/getAllVouchers/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> getVoucherById(@PathVariable Long voucherId) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.getVoucherByIdByShopFromUser(voucherId, user.getId()));
    }

    @PostMapping("/getAllVouchers/updateVoucher/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> updateVoucher(
            @PathVariable Long voucherId,
            @RequestBody VoucherDTO voucherDTO) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.updateVoucherByIdByShopFromUser(voucherId, user.getId(), voucherDTO));
    }

    @DeleteMapping("/getAllVouchers/deleteVoucher/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<String> deleteVoucher(@PathVariable Long voucherId) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        iVoucherService.deleteVoucherByIdByShopFromUser(voucherId, user.getId());
        return CommonResult.success("Voucher with ID " + voucherId + " has been deleted.");
    }

    @PatchMapping("/getAllVouchers/setActive/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> setVoucherActive(@PathVariable Long voucherId) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.setVoucherActive(voucherId, user.getId()));
    }

    @PatchMapping("/getAllVouchers/setInactive/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> setVoucherInactive(@PathVariable Long voucherId) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.setVoucherInactive(voucherId, user.getId()));
    }

    @PatchMapping("/getAllVouchers/switchStatus/{voucherId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<VoucherDTO> switchVoucherStatus(@PathVariable Long voucherId) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iVoucherService.switchVoucherStatus(voucherId, user.getId()));
    }

    // Public/Private status now will be set automatically by the system
    // So we don't need to set it manually

//    @PatchMapping("/getAllVouchers/setPublic/{voucherId}")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
//    public CommonResult<VoucherDTO> setVoucherPublic(@PathVariable Long voucherId) throws DataNotFoundException {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return CommonResult.success(iVoucherService.setVoucherPublic(voucherId, user.getId()));
//    }
//
//    @PatchMapping("/getAllVouchers/setPrivate/{voucherId}")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
//    public CommonResult<VoucherDTO> setVoucherPrivate(@PathVariable Long voucherId) throws DataNotFoundException {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return CommonResult.success(iVoucherService.setVoucherPrivate(voucherId, user.getId()));
//    }






//    @Autowired
//    public VoucherController(IVoucherService iVoucherService, VoucherMapper voucherMapper) {
//        this.iVoucherService = iVoucherService;
//        this.voucherMapper = voucherMapper;
//    }

//    @GetMapping
//    public CommonResult<List<VoucherDTO>> getAllVoucher() {
//        List<VoucherDTO> vouchers = iVoucherService.findAll().stream()
//                .map(voucherMapper::toDTO)
//                .collect(Collectors.toList());
//        return CommonResult.success(vouchers, "Get all vouchers successfully");
//    }
//
//    @GetMapping("/{id}")
//    public CommonResult<VoucherDTO> getVoucherById(@PathVariable Long id) {
//        return iVoucherService.findById(id)
//                .map(voucher -> CommonResult.success(voucherMapper.toDTO(voucher), "Get voucher successfully"))
//                .orElse(CommonResult.error(404, "Voucher not found"));
//    }
//
//    @PostMapping
//    public CommonResult<VoucherDTO> createVoucher(@RequestBody VoucherDTO voucherDTO) {
//        Voucher voucher = voucherMapper.toEntity(voucherDTO);
//        Voucher savedVoucher = iVoucherService.save(voucher);
//        return CommonResult.success(voucherMapper.toDTO(savedVoucher), "Create voucher successfully");
//    }
//
//    @PutMapping("/{id}")
//    public CommonResult<VoucherDTO> updateVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
//        return iVoucherService.findById(id)
//                .map(voucher -> {
//                    voucher.setCouponCode(voucherDetails.getCouponCode());
//                    voucher.setDiscountType(voucherDetails.getDiscountType());
//                    voucher.setDiscountValue(voucherDetails.getDiscountValue());
//                    voucher.setExpiredAt(voucherDetails.getExpiredAt());
//                    voucher.setActive(voucherDetails.isActive());
//                    voucher.setPublic(voucherDetails.isPublic());
//                    voucher.setName(voucherDetails.getName());
//                    voucher.setShopId(voucherDetails.getShopId());
//                    voucher.setStartAt(voucherDetails.getStartAt());
//                    voucher.setQuantity(voucherDetails.getQuantity());
//                    voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
//                    voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
//                    Voucher updatedVoucher = iVoucherService.save(voucher);
//                    return CommonResult.success(voucherMapper.toDTO(updatedVoucher), "Update voucher successfully");
//                }).orElse(CommonResult.error(404, "Voucher not found"));
//    }
//
//    @PatchMapping("/{id}")
//    public CommonResult<VoucherDTO> patchVoucher(@PathVariable Long id, @RequestBody VoucherDTO voucherDetails) {
//        Optional<Voucher> optionalVoucher = iVoucherService.findById(id);
//        if (optionalVoucher.isPresent()) {
//            Voucher voucher = optionalVoucher.get();
//            if (voucherDetails.getCouponCode() != null) voucher.setCouponCode(voucherDetails.getCouponCode());
//            if (voucherDetails.getDiscountType() != 0) voucher.setDiscountType(voucherDetails.getDiscountType());
//            if (voucherDetails.getDiscountValue() != null) voucher.setDiscountValue(voucherDetails.getDiscountValue());
//            if (voucherDetails.getExpiredAt() != null) voucher.setExpiredAt(voucherDetails.getExpiredAt());
//            if (voucherDetails.isActive() != voucher.isActive()) voucher.setActive(voucherDetails.isActive());
//            if (voucherDetails.isPublic() != voucher.isPublic()) voucher.setPublic(voucherDetails.isPublic());
//            if (voucherDetails.getName() != null) voucher.setName(voucherDetails.getName());
//            if (voucherDetails.getShopId() != null) voucher.setShopId(voucherDetails.getShopId());
//            if (voucherDetails.getStartAt() != null) voucher.setStartAt(voucherDetails.getStartAt());
//            if (voucherDetails.getQuantity() != 0) voucher.setQuantity(voucherDetails.getQuantity());
//            if (voucherDetails.getMinimumQuantityNeeded() != 0)
//                voucher.setMinimumQuantityNeeded(voucherDetails.getMinimumQuantityNeeded());
//            if (voucherDetails.isTypeRepeat() != voucher.isTypeRepeat())
//                voucher.setTypeRepeat(voucherDetails.isTypeRepeat());
//            Voucher updatedVoucher = iVoucherService.save(voucher);
//            return CommonResult.success(voucherMapper.toDTO(updatedVoucher), "Patch voucher successfully");
//        } else {
//            return CommonResult.error(404, "Voucher not found");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public CommonResult<String> deleteVoucher(@PathVariable Long id) {
//        return iVoucherService.findById(id)
//                .map(voucher -> {
//                    iVoucherService.deleteById(id);
//                    return CommonResult.success("Voucher with ID " + id + " has been deleted.");
//                })
//                .orElse(CommonResult.error(404, "Voucher not found"));
//    }
//
//    @GetMapping("/shop/{shopId}")
//    public CommonResult<List<VoucherDTO>> getVouchersByShopId(@PathVariable Long shopId) {
//        List<Voucher> vouchers = iVoucherService.findByShopId(shopId);
//        List<VoucherDTO> voucherDTOs = vouchers.stream().map(voucherMapper::toDTO).collect(Collectors.toList());
//        return CommonResult.success(voucherDTOs, "Get vouchers by shop ID successfully");
//    }
}
