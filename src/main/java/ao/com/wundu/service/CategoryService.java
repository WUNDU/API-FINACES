package ao.com.wundu.service;

import ao.com.wundu.dto.*;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryCreateDTO create);

    CategoryResponseDTO updateCategory(String id, String icon, CategoryUpdateDTO update);

    CategoryResponseDTO findCategoryById(String id);

    List<CategoryResponseDTO> findAllCategories();

    void deleteCategory(String id);
}
