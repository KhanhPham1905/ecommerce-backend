package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartMapper;

    private final ProductItemRepository productItemRepository;

    @Override
    public CartItem getCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found"));
    }

    @Override
    public void createCartItem(CartItemDTO cartItemDTO, Long userId) throws Exception {
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
    public Page<CartItem> getAllCartItems(PageRequest pageRequest, Long userId) throws Exception {
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
    public CartItem updateCartItem(Long id, CartItemDTO cartItemDTO, Long userId) throws Exception {
        return null;
    }

    @Override
    public Long getQuantityCartItem(Long userId) throws Exception {
        Long quantityCartItem = cartItemRepository.getQuantityCartItem(userId);
        return quantityCartItem;
    }

}
