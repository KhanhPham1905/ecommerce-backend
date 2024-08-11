package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartMapper;
    private final ProductItemRepository productItemRepository;

    @Override
    @Transactional
    public void addProductToCart(CartItemDTO cartItemDTO) {
        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        ProductItem productItem = productItemRepository.findById(cartItemDTO.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));
        // Kiểm tra số lượng sản phẩm
        if (productItem.getQuantity() < cartItemDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        // Cập nhật số lượng sản phẩm trong giỏ hàng nếu đã có
        CartItem existingCartItem = cartItemRepository.findByProductItemIdAndUserId(cartItemDTO.getProductItemId(), cartItemDTO.getUserId());
        if (existingCartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới
            CartItem cartItem = cartMapper.toEntity(cartItemDTO);
            // Lấy shopId và gán cho cartItem
            Long shopId = productItemRepository.findShopIdByProductItemId(cartItemDTO.getProductItemId());
            cartItem.setShopId(shopId);
            cartItem.setStatus(true); // Đặt giá trị status trước khi lưu
            cartItemRepository.save(cartItem);
        }

        // Cập nhật số lượng sản phẩm trong kho
        productItem.setQuantity(productItem.getQuantity() - cartItemDTO.getQuantity());
        productItemRepository.save(productItem);
    }

    @Transactional
    @Override
    public void updateCartItem(CartItemDTO cartItemDTO) {
        ProductItem productItem = productItemRepository.findById(cartItemDTO.getProductItemId()).orElseThrow(()
                -> new IllegalArgumentException("Product item not found"));
        if (productItem.getQuantity() < cartItemDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        CartItem existingCartItem = cartItemRepository.findById(cartItemDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        existingCartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(existingCartItem);
    }

    @Transactional
    @Override
    public void softDeleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cartItem.setStatus(false);  // Soft delete
        cartItemRepository.save(cartItem);
    }
}
