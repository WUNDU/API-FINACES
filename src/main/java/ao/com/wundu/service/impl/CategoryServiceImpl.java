package ao.com.wundu.service.impl;

import ao.com.wundu.application.dtos.CategoryCreateDTO;
import ao.com.wundu.application.dtos.CategoryResponseDTO;
import ao.com.wundu.application.dtos.CategoryUpdateDTO;
import ao.com.wundu.domain.entities.Category;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.CategoryNotFoundException;
import ao.com.wundu.domain.exceptions.TransactionNotFoundException;
import ao.com.wundu.infrastructure.repositories.CategoryRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import ao.com.wundu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private TransactionRepository transactionRepository;

        @Override
        public CategoryResponseDTO createCategory(String transactionId, CategoryCreateDTO create) {
                Transaction transaction = transactionRepository.findById(transactionId)
                                .orElseThrow(() -> new TransactionNotFoundException("Transação não encontrada"));

                Category category = new Category(create.nameCategory(), create.description(), transaction);
                category = categoryRepository.save(category);
                transaction.setCategory(category);
                transactionRepository.save(transaction);
                return new CategoryResponseDTO(category.getIdCategory(), category.getNameCategory(),
                                category.getDescription(), transaction.getId());
        }

        @Override
        public CategoryResponseDTO updateCategory(String id, CategoryUpdateDTO update) {
                Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada"));

                if (update.nameCategory() != null) {
                        category.setNameCategory(update.nameCategory());
                }

                category = categoryRepository.save(category);
                return new CategoryResponseDTO(
                                category.getIdCategory(),
                                category.getNameCategory(),
                                category.getDescription(),
                                category.getTransaction() != null ? category.getTransaction().getId() : null);
        }

        @Override
        public CategoryResponseDTO findCategoryById(String id) {
                Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada"));
                return new CategoryResponseDTO(
                                category.getIdCategory(),
                                category.getNameCategory(),
                                category.getDescription(),
                                category.getTransaction() != null ? category.getTransaction().getId() : null);
        }

        @Override
        public List<CategoryResponseDTO> findAllCategories() {
                return categoryRepository.findAll().stream()
                                .map(category -> new CategoryResponseDTO(
                                                category.getIdCategory(),
                                                category.getNameCategory(),
                                                category.getDescription(),
                                                category.getTransaction() != null ? category.getTransaction().getId()
                                                                : null))
                                .collect(Collectors.toList());
        }

        @Override
        public void deleteCategory(String id) {
                if (!categoryRepository.existsById(id))
                        throw new CategoryNotFoundException("Categoria não encontrada");
                categoryRepository.deleteById(id);
        }
}
