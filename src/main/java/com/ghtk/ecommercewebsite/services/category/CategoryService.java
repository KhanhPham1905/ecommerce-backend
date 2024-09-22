package com.ghtk.ecommercewebsite.services.category;

import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category createCategory(CategoryDTO categoryDTO, Long userId);
    Page<Category> getAllCategories(PageRequest pageRequest, String name);
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO,Long id);
    Category deleteCategory(Long id, Long userId);
}
