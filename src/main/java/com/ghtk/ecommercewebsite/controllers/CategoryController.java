package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public CommonResult<Category> getCategoryById(
            @PathVariable("id") Long categoryId
    )throws Exception{
        Category existingCategory = categoryService.getCategoryById(categoryId);
        return CommonResult.success(existingCategory, "Get category successfully");
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Object> createCategory (
            @Valid @RequestBody CategoryDTO categoryDTO
    ) throws Exception {
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.createCategory(categoryDTO, user.getId());
        return CommonResult.success(category,"Create category successfully");
    }

    @GetMapping("")
    public CommonResult<Page<Category>> getAllCatgories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "",required = false) String name
    ) throws Exception {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Category> categoriesPages = categoryService.getAllCategories(pageRequest,user.getId(), name);
        return CommonResult.success(categoriesPages, "Get all categories");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public  CommonResult deleteCategory(@PathVariable Long id) throws Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categoryService.deleteCategory(id, user.getId());
        return CommonResult.success("Delete success category");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Category> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    )throws Exception{
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.updateCategory(id, categoryDTO, user.getId());
        return CommonResult.success(category, "Update category successfully");
    }
}
