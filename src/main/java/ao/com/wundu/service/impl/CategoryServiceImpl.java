package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.Category;
import ao.com.wundu.repository.CategoryRepository;
import ao.com.wundu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateDTO create) {
        Category category = new Category(create.nameCategory());
        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category.getIdCategory().toString(), category.getNameCategory(),
                category.getIcon(), List.of());
    }

    @Override
    public CategoryResponseDTO updateCategory(String id, String icon, CategoryUpdateDTO update) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        if (update.nameCategory() != null) {
            category.setNameCategory(update.nameCategory());
        }

        category = categoryRepository.save(category);
        return new CategoryResponseDTO(
                category.getIdCategory().toString(),
                category.getNameCategory(),
                category.getIcon(),
                category.getTransactions().stream()
                        .map(transaction -> transaction.getId())
                        .collect(Collectors.toList()));
    }

    @Override
    public CategoryResponseDTO findCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        return new CategoryResponseDTO(
                category.getIdCategory().toString(),
                category.getNameCategory(),
                category.getIcon(),
                category.getTransactions().stream()
                        .map(transaction -> transaction.getId())
                        .collect(Collectors.toList()));
    }

    @Override
    public List<CategoryResponseDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponseDTO(
                        category.getIdCategory().toString(),
                        category.getNameCategory(),
                        category.getIcon(),
                        category.getTransactions().stream()
                                .map(transaction -> transaction.getId())
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id))
            throw new IllegalArgumentException("Categoria não encontrada");
        categoryRepository.deleteById(id);
    }
}
