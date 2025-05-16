package ao.com.wundu.application.usercases.category;

import ao.com.wundu.application.dtos.category.CategoryResponseDTO;
import ao.com.wundu.domain.entities.Category;
import ao.com.wundu.domain.exceptions.CategoryNotFoundException;
import ao.com.wundu.infrastructure.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FindCategoryUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindCategoryUseCase.class);

    private final CategoryRepository categoryRepository;

    public FindCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO execute(String id) {
        logger.info("Buscando categoria: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Categoria não encontrada: {}", id);
                    return new CategoryNotFoundException("Categoria não encontrada");
                });

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getTransaction() != null ? category.getTransaction().getId() : null
        );
    }
}
