package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.VoucherMapper;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements IVoucherService {

    private final ShopRepository shopRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

//    @Autowired
//    public VoucherServiceImpl(VoucherRepository voucherRepository) {
//        this.voucherRepository = voucherRepository;
//    }


    @Override
    public List<VoucherDTO> findAllVouchersByShopFromUser(Long userId) throws DataNotFoundException {
//        Long shopId = shopRepository.findShopByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("No shop found with this user")).getId();
//        Shop shopFound = shopRepository.findById(shopId)
//                .orElseThrow(() -> new DataNotFoundException("Shop not found"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));

        List<Voucher> vouchers = voucherRepository.findByShopId(shop.getId());
        return vouchers.stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherDTO createNewVoucher(VoucherDTO voucherDTO, Long userId) throws DataNotFoundException {
//        Long shopId = shopRepository.findShopByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("No shop found with this user")).getId();
//        Shop shopFound = shopRepository.findById(shopId)
//                .orElseThrow(() -> new DataNotFoundException("Shop not found"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));

        Voucher voucher = voucherMapper.toEntity(voucherDTO);
        voucher.setShopId(shop.getId());
        voucherRepository.save(voucher);
        return voucherMapper.toDTO(voucher);
    }

    @Override
    public VoucherDTO getVoucherByIdByShopFromUser(Long voucherId, Long userId) throws DataNotFoundException {
//        Long shopId = shopRepository.findShopByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("No shop found with this user")).getId();
//        Shop shopFound = shopRepository.findById(shopId)
//                .orElseThrow(() -> new DataNotFoundException("Shop not found"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DataNotFoundException("Voucher not found with this id"));
        return voucherMapper.toDTO(voucher);
    }

    @Override
    public VoucherDTO updateVoucherByIdByShopFromUser(Long voucherId, Long userId, VoucherDTO voucherDTO) throws DataNotFoundException {
//        Long shopId = shopRepository.findShopByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("No shop found with this user")).getId();
//        Shop shopFound = shopRepository.findById(shopId)
//                .orElseThrow(() -> new DataNotFoundException("Shop not found"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DataNotFoundException("Voucher not found with this id"));

        if (!voucher.getShopId().equals(shop.getId())) {
            throw new DataNotFoundException("You cannot edit this voucher");
        }

        updateVoucherFromVoucherDTO(voucherDTO, voucher);
        // Update is in this part
        voucherRepository.save(voucher);
        return voucherMapper.toDTO(voucher);
    }

    @Override
    public void deleteVoucherByIdByShopFromUser(Long voucherId, Long userId) throws DataNotFoundException {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DataNotFoundException("Voucher not found with this id"));

        if (!voucher.getShopId().equals(shop.getId())) {
            throw new DataNotFoundException("You cannot delete this voucher");
        }

        voucherRepository.deleteById(voucherId);
    }

    @Override
    public VoucherDTO setVoucherActive(Long voucherId, Long userId) throws DataNotFoundException {
        return updateVoucherStatus(voucherId, userId, true, null);
    }

    @Override
    public VoucherDTO setVoucherInactive(Long voucherId, Long userId) throws DataNotFoundException {
        return updateVoucherStatus(voucherId, userId, false, null);
    }

    @Override
    public VoucherDTO setVoucherPublic(Long voucherId, Long userId) throws DataNotFoundException {
        return updateVoucherStatus(voucherId, userId, null, true);
    }

    @Override
    public VoucherDTO setVoucherPrivate(Long voucherId, Long userId) throws DataNotFoundException {
        return updateVoucherStatus(voucherId, userId, null, false);
    }

    @Override
    public List<VoucherDTO> findAllVouchersByShopId(Long shopId) {
        List<Voucher> vouchers = voucherRepository.findByShopIdAndIsActiveAndIsPublic(shopId, true, true);
        return vouchers.stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }

    private VoucherDTO updateVoucherStatus(Long voucherId, Long userId, Boolean isActive, Boolean isPublic) throws DataNotFoundException {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("No shop found with this user"));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DataNotFoundException("Voucher not found with this id"));

        if (!voucher.getShopId().equals(shop.getId())) {
            throw new DataNotFoundException("You cannot modify this voucher");
        }

        if (isActive != null) {
            voucher.setActive(isActive);
        }
        if (isPublic != null) {
            voucher.setPublic(isPublic);
        }

        voucherRepository.save(voucher);
        return voucherMapper.toDTO(voucher);
    }

    public static void updateVoucherFromVoucherDTO(VoucherDTO voucherDTO, Voucher voucher) {
        voucher.setCouponCode(voucherDTO.getCouponCode());
        voucher.setDiscountType(voucherDTO.getDiscountType());
        voucher.setDiscountValue(voucherDTO.getDiscountValue());
        voucher.setExpiredAt(voucherDTO.getExpiredAt());
        voucher.setActive(voucherDTO.isActive());
        voucher.setPublic(voucherDTO.isPublic());
        voucher.setMaximumDiscountValue(voucherDTO.getMaximumDiscountValue());
        voucher.setName(voucherDTO.getName());
        voucher.setStartAt(voucherDTO.getStartAt());
        voucher.setQuantity(voucherDTO.getQuantity());
        voucher.setMinimumQuantityNeeded(voucherDTO.getMinimumQuantityNeeded());
        voucher.setTypeRepeat(voucherDTO.isTypeRepeat());
//        return voucher;
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
