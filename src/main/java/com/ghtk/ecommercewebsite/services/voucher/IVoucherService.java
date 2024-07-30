package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.models.entities.Voucher;

import java.util.List;
import java.util.Optional;

public interface IVoucherService {


    List<Voucher> findAll();

    Optional<Voucher> findById(Long id);

    Voucher save(Voucher voucher);

    void deleteById(Long id);
}
