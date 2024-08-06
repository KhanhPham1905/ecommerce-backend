package com.ghtk.ecommercewebsite.services.category;

import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id) throws Exception;
    Category createCategory(CategoryDTO categoryDTO, Long userId) throws Exception;
    List<Category> getAllCategories(Long userId) throws Exception;
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO,Long id) throws  Exception;
    Category deleteCategory(Long id, Long userId) throws Exception;

}
