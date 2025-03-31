package ao.com.wundu.service;

import ao.com.wundu.dto.*;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(String transactionId, CategoryCreateDTO create);

    CategoryResponseDTO updateCategory(String id, CategoryUpdateDTO update);

    CategoryResponseDTO findCategoryById(String id);

    List<CategoryResponseDTO> findAllCategories();

    void deleteCategory(String id);
}
