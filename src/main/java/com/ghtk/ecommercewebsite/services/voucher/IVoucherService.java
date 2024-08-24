package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Voucher;

import java.util.List;
import java.util.Optional;

public interface IVoucherService {
    List<VoucherDTO> findAllVouchersByShopFromUser(Long userId) throws DataNotFoundException;

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
