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
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO, Long userId){
            Category newCategory = Category
                    .builder()
                    .isDelete(Boolean.FALSE)
                    .name(categoryDTO.getName())
                    .status(categoryDTO.getStatus())
                    .slug(categoryDTO.getSlug())
                    .build();
            return categoryRepository.save(newCategory);
    }

    @Override
    public Page<Category> getAllCategories(PageRequest pageRequest, String name){
        return categoryRepository.findByShopId(name, pageRequest);
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO, Long userId){
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setStatus(categoryDTO.getStatus());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public Category deleteCategory(Long id, Long userId){
            Category category = getCategoryById(id);
            category.setIsDelete(Boolean.TRUE);
            categoryRepository.save(category);
            productRepository.softDeleteProductByCategoryId(id);
            productAttributesRepository.softDeleteProductAttributesByProductId(id);
            productItemRepository.softDeleteProductItemByProductId(id);
        return category;
    }
}
