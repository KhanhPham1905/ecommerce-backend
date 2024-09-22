package com.ghtk.ecommercewebsite.services.productAttributes;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.exceptions.QuantityExceededException;
import com.ghtk.ecommercewebsite.mapper.ProductAttributesMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductAttributesDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.repositories.ProductAttributesRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import org.springframework.security.access.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
    public ProductAttributesDTO createProductAttributes(ProductAttributesDTO productAttributesDTO, Long id, Long userId){
        List<ProductAttributes> ListProductAttributes = productAttributesRepository.findByProductId(id);
        if (ListProductAttributes.size() > 5){
            throw new QuantityExceededException("you can only have a maximum of 4 attributes");
        }

        ProductAttributes productAttributes = productAttributesMapper.toEntity(productAttributesDTO);
        productAttributes.setIsDelete(Boolean.FALSE);
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
    public ProductAttributesDTO getProductAttributesById(Long id){
        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product attributes by id"));
        return productAttributesMapper.toDTO(productAttributes);
    }

    @Override
    @Transactional
    public ProductAttributesDTO updateProductAttributes(ProductAttributesDTO productAttributesDTO, Long userId){
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
    public ProductAttributesDTO deleteProductAttributes(Long id, Long userId){
        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product attributes by id"));
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by id"));
        Product product = productRepository.findById(productAttributes.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product by id"));
        if (!product.getShopId().equals(shop.getId())) {
            throw new AccessDeniedException("User does not have access to this product.");
        }
        productAttributes.setIsDelete(Boolean.TRUE);
        productAttributesRepository.save(productAttributes);
        return productAttributesMapper.toDTO(productAttributes);
    }

    @Override
    public List<ProductAttributes> getAllProductAttributes(Long idProduct){
        List<ProductAttributes> productAttributes = productAttributesRepository.findByProductId(idProduct);
        return productAttributes;
    }
}
