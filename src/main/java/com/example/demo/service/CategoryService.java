package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
/**
 * Service class for managing categories.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService; // Inject ProductService

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(@NotNull Category category) {
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " + category.getId() + " does not exist."));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
    }

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category with ID " + id + " does not exist.");
        }

        // Get the category
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category with ID " + id + " does not exist."));

        // Get products related to this category
        List<Product> products = productService.getProductsByCategoryId(id);

        // Delete related products
        for (Product product : products) {
            productService.deleteProductById(product.getId());
        }

        // Delete the category
        categoryRepository.deleteById(id);
    }
}

