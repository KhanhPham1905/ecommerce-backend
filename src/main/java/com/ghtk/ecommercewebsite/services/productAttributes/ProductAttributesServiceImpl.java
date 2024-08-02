package com.ghtk.ecommercewebsite.services.productAttributes;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.ProductAttributesMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductAttributesDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.repositories.ProductAttributesRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributesServiceImpl implements ProductAttributesService
{
    private  final ProductAttributesRepository productAttributesRepository;
    private final ProductAttributesMapper productAttributesMapper;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public ProductAttributesDTO createProductAttributes(ProductAttributesDTO productAttributesDTO, Long id, Long userId) throws Exception{
        ProductAttributes productAttributes = productAttributesMapper.toEntity(productAttributesDTO);
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by id"));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product by id"));
        if (!product.getShopId().equals(shop.getId())) {
            throw new AccessDeniedException("User does not have access to this product.");
        }


        return productAttributesMapper.toDTO(productAttributesRepository.save(productAttributes));
    }

    @Override
    public ProductAttributesDTO getProductAttributesById(Long id) throws Exception {
        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product attributes by id"));
        ProductAttributesDTO productAttributes1 = productAttributesMapper.toDTO(productAttributes);
        return productAttributesMapper.toDTO(productAttributes);
    }

    @Override
    @Transactional
    public ProductAttributesDTO updateProductAttributes(ProductAttributesDTO productAttributesDTO, Long userId) throws Exception {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by id"));
        Product product = productRepository.findById(productAttributesDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product by id"));
        if (!product.getShopId().equals(shop.getId())) {
            throw new AccessDeniedException("User does not have access to this product.");
        }
        ProductAttributes productAttributes = productAttributesRepository.findById(productAttributesDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product attributes by id"));
        productAttributes.setName(productAttributesDTO.getName());
        productAttributesRepository.save(productAttributes);
        return productAttributesMapper.toDTO(productAttributes);
    }

    @Override
    @Transactional
    public ProductAttributesDTO deleteProductAttributes(Long id, Long userId) throws Exception {
        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product attributes by id"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by id"));
        Product product = productRepository.findById(productAttributes.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product by id"));
        if (!product.getShopId().equals(shop.getId())) {
            throw new AccessDeniedException("User does not have access to this product.");
        }
        productAttributesRepository.deleteById(id);
        return productAttributesMapper.toDTO(productAttributes);
    }

    @Override
    public List<ProductAttributes> getAllProductAttributes(Long idProduct) throws Exception {
        List<ProductAttributes> productAttributes = productAttributesRepository.findByProductId(idProduct);
        return productAttributes;
    }
}
