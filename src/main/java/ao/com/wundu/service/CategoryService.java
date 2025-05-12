package ao.com.wundu.service;

import ao.com.wundu.application.dtos.CategoryCreateDTO;
import ao.com.wundu.application.dtos.CategoryResponseDTO;
import ao.com.wundu.application.dtos.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(String transactionId, CategoryCreateDTO create);

    CategoryResponseDTO updateCategory(String id, CategoryUpdateDTO update);

    CategoryResponseDTO findCategoryById(String id);

    List<CategoryResponseDTO> findAllCategories();

    void deleteCategory(String id);
}
