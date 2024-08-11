package com.ghtk.ecommercewebsite.services.product;

import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<Product> findAllActive() {
        return productRepository.findByStatusTrue();
    }

    @Override
    public Optional<Product> findActiveById(Long id) {
        return productRepository.findByIdAndStatusTrue(id);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    updateProductDetails(product, productDetails);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private void updateProductDetails(Product product, Product productDetails) {
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setSlug(productDetails.getSlug());
        product.setTotalSold(productDetails.getTotalSold());
        product.setProductView(productDetails.getProductView());
        product.setStatus(productDetails.getStatus());
        product.setBrandId(productDetails.getBrandId());
        product.setShopId(productDetails.getShopId());
    }

    @Override
    @Transactional
    public Product patchProduct(Long id, Map<String, Object> updates) {
        return productRepository.findById(id)
                .map(product -> {
                    updates.forEach((key, value) -> applyPatch(product, key, value));
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private void applyPatch(Product product, String key, Object value) {
        switch (key) {
            case "name":
                product.setName((String) value);
                break;
            case "description":
                product.setDescription((String) value);
                break;
            case "slug":
                product.setSlug((String) value);
                break;
            case "totalSold":
                product.setTotalSold(((Number) value).longValue());
                break;
            case "productView":
                product.setProductView((Integer) value);
                break;
            case "status":
                product.setStatus((Boolean) value);
                break;
            case "brandId":
                product.setBrandId(((Number) value).longValue());
                break;
            case "shopId":
                product.setShopId(((Number) value).longValue());
                break;
        }
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus(false); // Xóa mềm Product
            List<ProductItem> productItems = productItemRepository.findByProductId(product.getId());

            // Xóa mềm tất cả các ProductItem liên kết
            productItems.forEach(item -> {
                item.setStatus(false);
                productItemRepository.save(item);
                // Xóa mềm tất cả các CartItem liên kết
                List<CartItem> cartItems = cartItemRepository.findByProductItemId(item.getId());
                cartItems.forEach(cartItem -> {
                    cartItem.setStatus(false);
                    cartItemRepository.save(cartItem);
                });
            });
            // Lưu lại trạng thái xóa mềm của Product
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .filter(product -> Boolean.TRUE.equals(product.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchProductsByDes(String keyword) {
        return productRepository.findByDescriptionContaining(keyword).stream()
                .filter(product -> Boolean.TRUE.equals(product.getStatus()))
                .collect(Collectors.toList());
    }
}
