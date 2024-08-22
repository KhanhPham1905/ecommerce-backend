package com.ghtk.ecommercewebsite.services.voucher;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.VoucherMapper;
import com.ghtk.ecommercewebsite.models.dtos.VoucherDTO;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements IVoucherService {

    private final ShopRepository shopRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

//    @Scheduled(fixedRate = 60000)
//    @Transactional
//    public void makeVouchersPublic() {
//        voucherRepository.findAllByIsActiveTrueAndIsPublicFalse()
//                .forEach(voucher -> {
//                    if (LocalDateTime.now().isAfter(voucher.getStartAt())) {
//                        voucher.setPublic(true);
//                        voucherRepository.save(voucher);
//                    }
//                });
//    }

    // Set the default value "NONE" for existed vouchers (whose discount type is null)
    // Uncomment the following methods and repository method in VoucherRepository

//    @PostConstruct
//    public void updateDefaultDiscountTypeOnStartup() {
//        updateDiscountTypeToNone();
//    }
//
//    @Transactional
//    public void updateDiscountTypeToNone() {
//        voucherRepository.updateDiscountTypeToNone();
//    }

    // Old scheduling version
    @Scheduled(fixedRate = 60000) // 1 minute
    @Transactional
    public void updateVouchersPublicStatus() {
        List<Voucher> vouchers = voucherRepository.findAllByIsActiveTrue();

        for (Voucher voucher : vouchers) {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            ZonedDateTime startAt = ZonedDateTime.of(voucher.getStartAt(), ZoneId.of("Asia/Ho_Chi_Minh"));
            ZonedDateTime expiredAt = ZonedDateTime.of(voucher.getExpiredAt(), ZoneId.of("Asia/Ho_Chi_Minh"));

            // Testing
//            System.out.println("Current Time: " + now);
//            System.out.println("Voucher Start Time: " + voucher.getStartAt());
//            System.out.println("Voucher Expiry Time: " + voucher.getExpiredAt());

            if (now.isAfter(startAt) && now.isBefore(expiredAt)) {
                if (!voucher.isPublic()) {
                    voucher.setPublic(true);
                    voucherRepository.save(voucher);
                }
            }

            if (now.isAfter(expiredAt)) {
                if (voucher.isPublic()) {
                    voucher.setPublic(false);
                    voucherRepository.save(voucher);
                }

                switch (voucher.getTypeRepeat()) {
                    case DAILY:
//                        voucher.setStartAt(voucher.getStartAt().plusDays(1));
//                        voucher.setExpiredAt(voucher.getExpiredAt().plusDays(1));
                        voucher.setStartAt(voucher.getStartAt().plusMinutes(1));
                        voucher.setExpiredAt(voucher.getExpiredAt().plusMinutes(1));
                        break;
                    case WEEKLY:
                        voucher.setStartAt(voucher.getStartAt().plusWeeks(1));
                        voucher.setExpiredAt(voucher.getExpiredAt().plusWeeks(1));
                        break;
                    case MONTHLY:
                        voucher.setStartAt(voucher.getStartAt().plusMonths(1));
                        voucher.setExpiredAt(voucher.getExpiredAt().plusMonths(1));
                        break;
                }
                voucherRepository.save(voucher);
            }
        }
    }

    // Another version
//    @Scheduled(fixedRate = 10000) // 1 minute
//    @Transactional
//    public void updateVouchersPublicStatusBetterVersion() {
//        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
//        List<Voucher> vouchers = voucherRepository.findAllByIsActiveTrueAndExpiredAtAfter(now.toLocalDateTime());
//
//        for (Voucher voucher : vouchers) {
//            ZonedDateTime startAt = ZonedDateTime.of(voucher.getStartAt(), ZoneId.of("Asia/Ho_Chi_Minh"));
//            ZonedDateTime expiredAt = ZonedDateTime.of(voucher.getExpiredAt(), ZoneId.of("Asia/Ho_Chi_Minh"));
//
//            if (!voucher.isPublic() && now.isAfter(startAt) && now.isBefore(expiredAt)) {
//                voucher.setPublic(true);
//                voucherRepository.save(voucher);
//            }
//
//            if (voucher.isPublic() && now.isAfter(expiredAt)) {
//                voucher.setPublic(false);
//                voucherRepository.save(voucher);
//                switch (voucher.getTypeRepeat()) {
//                    case DAILY:
//                        voucher.setStartAt(voucher.getStartAt().plusDays(1));
//                        voucher.setExpiredAt(voucher.getExpiredAt().plusDays(1));
//                        break;
//                    case WEEKLY:
//                        voucher.setStartAt(voucher.getStartAt().plusWeeks(1));
//                        voucher.setExpiredAt(voucher.getExpiredAt().plusWeeks(1));
//                        break;
//                    case MONTHLY:
//                        voucher.setStartAt(voucher.getStartAt().plusMonths(1));
//                        voucher.setExpiredAt(voucher.getExpiredAt().plusMonths(1));
//                        break;
//                }
//            }
//        }
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
        voucher.setTypeRepeat(voucherDTO.getTypeRepeat());
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
