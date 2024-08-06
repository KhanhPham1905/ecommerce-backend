package com.ghtk.ecommercewebsite.services.productitem;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.DetailProductItemDTO;
import com.ghtk.ecommercewebsite.models.dtos.ProductItemAttributesDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.models.entities.ProductItemAttributes;
import com.ghtk.ecommercewebsite.repositories.ProductItemAttributesRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService
{
    private final ProductItemRepository productItemRepository;
    private final ProductItemAttributesRepository productItemAttributesRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public DetailProductItemDTO createProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws Exception {
        ProductItem productItem = ProductItem.builder()
                .productId(detailProductItemDTO.getProductId())
                .price(detailProductItemDTO.getPrice())
                .skuCode(detailProductItemDTO.getSkuCode())
                .build();
        productItemRepository.save(productItem);

        for(ProductItemAttributesDTO productItemAttributesDTO : detailProductItemDTO.getProductItemAtrAttributesDTOS()){
            ProductItemAttributes productItemAttributes = ProductItemAttributes.builder()
                    .productItemId(productItem.getId())
                    .productAttributesId(productItemAttributesDTO.getProductAttributesId())
                    .value(productItemAttributesDTO.getValue())
                    .build();
            productItemAttributesRepository.save(productItemAttributes);
        }
        return detailProductItemDTO;
    }

    @Override
    public Object getAllProductItem(Long productId, Long userId) throws Exception {
            List<ProductItem> productItems =  productItemRepository.findAllByProductId(productId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(()->new DataNotFoundException("Cannot find product"));

            List<Object> productItemValues =  new ArrayList<>();
            for (ProductItem productItem :productItems) {
                List<ProductItemAttributes> attributeValues = productItemAttributesRepository.findByProductItemId(productItem.getId());
                List<ProductItemAttributesDTO> attributeDTOs = attributeValues.stream()
                        .map(attr -> new ProductItemAttributesDTO(attr.getValue(),attr.getProductAttributesId(), attr.getId()))
                        .collect(Collectors.toList());

                DetailProductItemDTO detailProductItemDTO = DetailProductItemDTO.builder()
                        .quantity(productItem.getQuantity())
                        .name(product.getName())
                        .skuCode(productItem.getSkuCode())
                        .price(productItem.getPrice())
                        .productId(productItem.getProductId())
                        .productItemAtrAttributesDTOS(attributeDTOs)
                        .build();
                productItemValues.add(detailProductItemDTO);
            }

        return productItemValues;
    }

    @Override
    @Transactional
    public DetailProductItemDTO updateProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws Exception {
        ProductItem productItem = productItemRepository.findBySkuCode(detailProductItemDTO.getSkuCode());
        if (productItem == null){
            throw new DataNotFoundException("Cannot find product item");
        }
        ProductItem newProductItem = ProductItem.builder()
                .skuCode(detailProductItemDTO.getSkuCode())
                .id(productItem.getId())
                .productId(detailProductItemDTO.getProductId())
                .price(detailProductItemDTO.getPrice())
                .build();

        productItemRepository.save(newProductItem);

        for(ProductItemAttributesDTO productItemAttributesDTO : detailProductItemDTO.getProductItemAtrAttributesDTOS()) {
            ProductItemAttributes productItemAttributes = ProductItemAttributes.builder()
                    .value(productItemAttributesDTO.getValue())
                    .id(productItemAttributesDTO.getId())
                    .productAttributesId(productItemAttributesDTO.getProductAttributesId())
                    .productItemId(productItem.getId())
                    .build();
            productItemAttributesRepository.save(productItemAttributes);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProductItem(Long id, Long userId) throws Exception {
        productItemRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("product item not exist"));
        productItemRepository.deleteById(id);
        List<ProductItemAttributes> productItemAttributes = productItemAttributesRepository.findByProductItemId(id);
        for(ProductItemAttributes productItemAttribute: productItemAttributes ){
            productItemAttributesRepository.deleteById(productItemAttribute.getId());
        }
    }

    @Override
    public DetailProductItemDTO getProductItemById(Long id, Long userId) throws Exception {
        ProductItem productItem = productItemRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find product item by id"));
        Product product = productRepository.findById(productItem.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find product item by id"));
        List<ProductItemAttributes> attributeValues = productItemAttributesRepository.findByProductItemId(productItem.getId());
        List<ProductItemAttributesDTO> attributeDTOs = attributeValues.stream()
                .map(attr -> new ProductItemAttributesDTO(attr.getValue(),attr.getProductAttributesId(), attr.getId()))
                .collect(Collectors.toList());

        DetailProductItemDTO detailProductItemDTO = DetailProductItemDTO.builder()
                .quantity(productItem.getQuantity())
                .name(product.getName())
                .skuCode(productItem.getSkuCode())
                .price(productItem.getPrice())
                .productId(productItem.getProductId())
                .productItemAtrAttributesDTOS(attributeDTOs)
                .build();
        return detailProductItemDTO;
    }
}
