package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartMapper;
    private final ProductItemRepository productItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartItemMapper cartMapper, ProductItemRepository productItemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.productItemRepository = productItemRepository;
    }

    @Override
    @Transactional
    public CartItem save(CartItem cartItem) {
        Long shopId = productItemRepository.findShopIdByProductItemId(cartItem.getProductItemId());
        if (shopId == null) {
            throw new IllegalArgumentException("Product item not found or no associated shop");
        }
        // Gán giá trị shopId cho cartItem
        cartItem.setShopId(shopId);
        // Lưu cartItem
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public void addProductToCart(@RequestBody CartItemDTO cartItemDTO) {
        // Kiểm tra số lượng sản phẩm
        ProductItem productItem = productItemRepository.findById(cartItemDTO.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));

        if (productItem.getQuantity() < cartItemDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        productItemRepository.save(productItem);
        // Chuyển đổi DTO thành Entity
        CartItem cartItem = cartMapper.toEntity(cartItemDTO);
        // Lấy shopId và gán cho cartItem
        Long shopId = productItemRepository.findShopIdByProductItemId(cartItemDTO.getProductItemId());
        cartItem.setShopId(shopId);
        // Lưu cartItem vào cơ sở dữ liệu
        cartItemRepository.save(cartItem);
    }
    @Override
    @Transactional
    public void deleteCartItems(@RequestParam Long userId, @RequestParam Long productItemId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productItemId);
    }
}
