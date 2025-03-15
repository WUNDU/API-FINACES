package ao.com.wundu.controller;

import ao.com.wundu.dto.CategoryCreateDTO;
import ao.com.wundu.dto.CategoryResponseDTO;
import ao.com.wundu.dto.CategoryUpdateDTO;
import ao.com.wundu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryCreateDTO create) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(create));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable String id, @PathVariable String icon,
            @RequestBody CategoryUpdateDTO update) {
        return ResponseEntity.ok(categoryService.updateCategory(id, icon, update));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
