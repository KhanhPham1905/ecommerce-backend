package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.models.enums.DiscountType;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    private final VoucherRepository voucherRepository;

    private final CartItemMapper cartMapper;

    private final ProductItemRepository productItemRepository;

    @Override
    public CartItem getCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found"));
    }

    @Override
    public void createCartItem(CartItemDTO cartItemDTO, Long userId) {
        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        ProductItem productItem = productItemRepository.findById(cartItemDTO.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));
        // Kiểm tra số lượng sản phẩm
        if (productItem.getQuantity() < cartItemDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        // Cập nhật số lượng sản phẩm trong giỏ hàng nếu đã có
        CartItem existingCartItem = cartItemRepository.findByProductItemIdAndUserId(cartItemDTO.getProductItemId(), userId);
        if (existingCartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới
            CartItem cartItem = cartMapper.toEntity(cartItemDTO);
            // Lấy shopId và gán cho cartItem
            Long shopId = productItemRepository.findShopIdByProductItemId(cartItemDTO.getProductItemId());
            cartItem.setUserId(userId);
            cartItem.setShopId(shopId);
            BigDecimal unitPrice = productItem.getPrice();
            BigDecimal finalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItemDTO.getQuantity()));
            cartItem.setTotalPrice(finalPrice);
            cartItemRepository.save(cartItem);
        }
    }

    private BigDecimal calculateDirectDiscount(Long voucherId, int quantity, BigDecimal unitPrice) {
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherId != null) {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
            if (isVoucherApplicable(voucher, quantity)) {
                discount = applyVoucher(voucher, unitPrice, quantity);
            }
        }
        return discount;
    }

    private boolean isVoucherApplicable(Voucher voucher, int quantity) {
        return voucher.getIsPublic() &&
                LocalDateTime.now().isBefore(voucher.getExpiredAt()) &&
                quantity >= voucher.getMinimumQuantityNeeded();
    }

    private BigDecimal applyVoucher(Voucher voucher, BigDecimal unitPrice, int quantity) {
        // Tính tổng giá trị giảm giá ban đầu là 0
        BigDecimal discount = BigDecimal.ZERO;

        // Kiểm tra loại giảm giá
        if (voucher.getDiscountType().equals(DiscountType.PERCENTAGE)) {
            // Giảm theo phần trăm dựa trên đơn giá và số lượng
            discount = unitPrice.multiply(BigDecimal.valueOf(quantity))
                    .multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        } else if (voucher.getDiscountType().equals(DiscountType.FIXEDAMOUNT)) {
            // Giảm theo giá trị cố định, nhân với số lượng
            discount = voucher.getDiscountValue().multiply(BigDecimal.valueOf(quantity));
        }

        // Nếu có giá trị giảm giá tối đa
        if (voucher.getMaximumDiscountValue() != null) {
            discount = discount.min(voucher.getMaximumDiscountValue().multiply(BigDecimal.valueOf(quantity)));
        }

        return discount;
    }


    @Override
    public Page<CartItem> getAllCartItems(PageRequest pageRequest, Long userId) {
        return cartItemRepository.findByUserId(userId, pageRequest);
    }

    @Override
    public void deleteCartItem(Long id, Long userId) throws Exception {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByIdAndUserId(id, userId);
        if (cartItemOptional.isPresent()) {
            cartItemRepository.delete(cartItemOptional.get());
        } else {
            throw new Exception("Cart item not found or does not belong to the user");
        }
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity, Long userId) throws Exception {
        // Lấy thông tin giỏ hàng dựa trên cartItemId và userId
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new Exception("Cart item not found"));
        // Cập nhật số lượng sản phẩm
        ProductItem productItem = productItemRepository.findById(cartItem.getProductItemId()).orElseThrow(() -> new IllegalArgumentException("Product item not found"));
        if (productItem.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        // Cập nhật số lượng sản phẩm
        cartItem.setQuantity(quantity);
        // Tính toán lại giá cuối cùng
        BigDecimal unitPrice = productItem.getPrice();
        BigDecimal discount = BigDecimal.ZERO;
        if (cartItem.getVoucherId() != null) {
            discount = applyVoucher(voucherRepository.findById(cartItem.getVoucherId()).orElseThrow(), unitPrice, quantity);
        }
        BigDecimal finalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discount);
        cartItem.setTotalPrice(finalPrice);
        // Lưu thay đổi
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long id, CartItemDTO cartItemDTO, Long userId) {
        return null;
    }

    @Override
    public Long getQuantityCartItem(Long userId) {
        return cartItemRepository.getQuantityCartItem(userId);
    }

    @Override
    @Transactional
    public void applyVoucherToCartItem(Long cartItemId, Long voucherId, Long userId) {
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem không tồn tại hoặc không thuộc về người dùng"));

        ProductItem productItem = productItemRepository.findById(cartItem.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));
        if (voucherId != null) {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new IllegalArgumentException("Voucher không tồn tại"));
            // Kiểm tra điều kiện của voucher
            if (!voucher.getIsPublic() || LocalDateTime.now().isAfter(voucher.getExpiredAt()))
                throw new IllegalArgumentException("Voucher không hợp lệ hoặc đã hết hạn");
            BigDecimal unitPrice = productItem.getPrice();
            BigDecimal discount = applyVoucher(voucher, unitPrice, cartItem.getQuantity());
            BigDecimal finalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())).subtract(discount);
            cartItem.setVoucherId(voucherId);
            cartItem.setTotalPrice(finalPrice);
            cartItemRepository.save(cartItem);
        } else {
            BigDecimal unitPrice = productItem.getPrice();
            BigDecimal finalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            cartItem.setTotalPrice(finalPrice);
            cartItemRepository.save(cartItem);
            cartItem.setVoucherId(null);
        }
    }
}
