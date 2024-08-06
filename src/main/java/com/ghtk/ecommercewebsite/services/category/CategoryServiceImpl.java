package com.ghtk.ecommercewebsite.services.category;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.repositories.CategoryRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.repositories.SellerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

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
                    .name(categoryDTO.getName())
                    .status(categoryDTO.getStatus())
                    .slug(categoryDTO.getSlug())
                    .shopId(shopId)
                    .build();
            return categoryRepository.save(newCategory);
    }

    @Override
    public List<Category> getAllCategories(Long userId) throws Exception{
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        if (shopId == null){
            throw new DataNotFoundException("Cannot find Shop id by Userid");
        }
        return categoryRepository.findByShopId(shopId);
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
        List<Product> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()){
            throw new IllegalStateException("Cannot delete category with associated products");
        }else{
            categoryRepository.deleteById(id);
        }
        return category;
    }
}
