package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.models.entities.Voucher;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            cartItemRepository.save(cartItem);
        }

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

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity, Long userId) throws Exception {
        // Lấy thông tin giỏ hàng dựa trên cartItemId và userId
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new Exception("Cart item not found"));
        // Cập nhật số lượng sản phẩm
        cartItem.setQuantity(quantity);
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
        // Lấy thông tin CartItem theo ID và UserID để đảm bảo tính xác thực
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem không tồn tại hoặc không thuộc về người dùng"));
        // Kiểm tra voucher có tồn tại không và có hợp lệ không
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher không tồn tại"));
        // Kiểm tra điều kiện áp dụng voucher (nếu có)
        if (!voucher.isPublic() || LocalDateTime.now().isAfter(voucher.getExpiredAt())) {
            throw new IllegalArgumentException("Voucher không hợp lệ hoặc đã hết hạn");
        }
        // Áp dụng voucher cho CartItem
        cartItem.setVoucherId(voucherId);
        cartItemRepository.save(cartItem);
    }
}
