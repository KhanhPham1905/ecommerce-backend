package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;

import java.util.List;
import java.util.Optional;

public interface IVoucherService {


    List<Voucher> findAll();

    Optional<Voucher> findById(Long id);

    Voucher save(Voucher voucher);

    void deleteById(Long id);

    List<Voucher> findByShopId(Long shopId) ;

    // Các phương thức mới
    List<VoucherDTO> getAllVouchers();
    VoucherDTO getVoucherById(Long id);
    VoucherDTO createVoucher(VoucherDTO voucherDTO);
    VoucherDTO updateVoucher(Long id, VoucherDTO voucherDetails);
    VoucherDTO patchVoucher(Long id, VoucherDTO voucherDetails);
    void softDeleteVoucher(Long id);
    List<VoucherDTO> getVouchersByShopId(Long shopId);
}
