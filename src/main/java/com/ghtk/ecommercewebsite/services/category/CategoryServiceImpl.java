package com.ghtk.ecommercewebsite.services.category;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductAttributesRepository productAttributesRepository;

    @Override
    public Category getCategoryById(Long id) throws Exception{
        return categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO, Long userId)  throws Exception {
            Long shopId = sellerRepository.findShopIdByUserId(userId);
            if (shopId == null){
                throw new DataNotFoundException("Cannot find Shop id by Userid");
            }
            Category newCategory = Category
                    .builder()
                    .isDelete(Boolean.FALSE)
                    .name(categoryDTO.getName())
                    .status(categoryDTO.getStatus())
                    .slug(categoryDTO.getSlug())
                    .shopId(shopId)
                    .build();
            return categoryRepository.save(newCategory);
    }

    @Override
    public Page<Category> getAllCategories(PageRequest pageRequest, Long userId, String name) throws Exception{
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        if (shopId == null){
            throw new DataNotFoundException("Cannot find Shop id by Userid");
        }
        return categoryRepository.findByShopId(shopId,name, pageRequest);
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO, Long userId) throws  Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category by id"));
        if(!category.getShopId().equals(shopId)) {
            throw new AccessDeniedException("account seller and shop not match");
        }
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setStatus(categoryDTO.getStatus());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public Category deleteCategory(Long id, Long userId) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category by id"));
        if(!category.getShopId().equals(shopId)) {
            throw new AccessDeniedException("Account seller and shop not match");
        }
//        List<Product> products = productRepository.findByCategoryId(id);

            category.setIsDelete(Boolean.TRUE);
            categoryRepository.save(category);
            productRepository.softDeleteProductByCategoryId(id);
            productAttributesRepository.softDeleteProductAttributesByProductId(id);
            productItemRepository.softDeleteProductItemByProductId(id);

        return category;
    }
}
