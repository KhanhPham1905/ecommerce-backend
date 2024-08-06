package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements IVoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }


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
}
