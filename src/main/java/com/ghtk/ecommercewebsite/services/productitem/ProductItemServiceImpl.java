package com.ghtk.ecommercewebsite.services.productitem;

import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.OrderItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService
{

    private final ProductItemRepository productItemRepository;

    private  final CartItemRepository cartItemRepository;

    private final OrderItemRepository orderItemRepository ;

    @Transactional
    public void deleteProductItemById(Long id) {
        // Xóa các bản ghi trong cart_item tham chiếu đến product_item
        cartItemRepository.deleteByProductItemId(id);
        // Xóa bản ghi trong product_item
        productItemRepository.deleteById(id);
    }

}
