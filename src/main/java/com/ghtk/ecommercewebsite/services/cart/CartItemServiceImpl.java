package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem getCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found"));
    }

    @Override
    public CartItem createCartItem(CartItemDTO cartItemDTO, Long userId) throws Exception {
        CartItem cartItem = CartItem.builder()
                .userId(userId)
                .productItemId(cartItemDTO.getProductItemId())
                .quantity(cartItemDTO.getQuantity())
                .voucherId(cartItemDTO.getVoucherId())
                .shopId(cartItemDTO.getShopId())
                .build();

        return cartItemRepository.save(cartItem);
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

    @Override
    public CartItem updateCartItem(Long id, CartItemDTO cartItemDTO, Long userId) throws Exception {
        CartItem existingCartItem = cartItemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Cart item not found or does not belong to the user"));

        existingCartItem.setProductItemId(cartItemDTO.getProductItemId());
        existingCartItem.setQuantity(cartItemDTO.getQuantity());
        existingCartItem.setVoucherId(cartItemDTO.getVoucherId());
        existingCartItem.setShopId(cartItemDTO.getShopId());

        return cartItemRepository.save(existingCartItem);
    }
}
