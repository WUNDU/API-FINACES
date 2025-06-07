package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.category.CategoryCreateDTO;
import ao.com.wundu.application.dtos.category.CategoryResponseDTO;
import ao.com.wundu.application.usercases.category.CreateCategoryUseCase;
import ao.com.wundu.application.usercases.category.FindCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Categories", description = "Operations related to transaction categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindCategoryUseCase findCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, FindCategoryUseCase findCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.findCategoryUseCase = findCategoryUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a new transaction category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryCreateDTO dto) {
        logger.info("Criando categoria: {}", dto.name());
        CategoryResponseDTO response = createCategoryUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a category by ID", description = "Retrieves a category by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponseDTO> findCategory(
            @Parameter(description = "Category ID", required = true, example = "cat1")
            @PathVariable String id) {
        logger.info("Buscando categoria: {}", id);
        CategoryResponseDTO response = findCategoryUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
