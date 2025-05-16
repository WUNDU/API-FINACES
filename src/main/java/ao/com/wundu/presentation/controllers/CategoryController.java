package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.category.CategoryCreateDTO;
import ao.com.wundu.application.dtos.category.CategoryResponseDTO;
import ao.com.wundu.application.usercases.category.CreateCategoryUseCase;
import ao.com.wundu.application.usercases.category.FindCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindCategoryUseCase findCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, FindCategoryUseCase findCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.findCategoryUseCase = findCategoryUseCase;
    }

    @PostMapping("/transaction/{transactionId}")
    @Operation(summary = "Create a category for a transaction")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @PathVariable String transactionId,
            @Valid @RequestBody CategoryCreateDTO dto
    ) {
        logger.info("Criando categoria para transação: {}", transactionId);
        CategoryResponseDTO response = createCategoryUseCase.execute(transactionId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a category by ID")
    @ApiResponse(responseCode = "200", description = "Category found")
    public ResponseEntity<CategoryResponseDTO> findCategory(@PathVariable String id) {
        logger.info("Buscando categoria: {}", id);
        CategoryResponseDTO response = findCategoryUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
