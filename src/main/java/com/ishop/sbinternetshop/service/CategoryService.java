package com.ishop.sbinternetshop.service;

import com.ishop.sbinternetshop.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);
}
