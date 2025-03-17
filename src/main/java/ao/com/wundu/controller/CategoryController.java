//package ao.com.wundu.controller;
//
//import ao.com.wundu.dto.*;
//import ao.com.wundu.service.CategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/categories")
//public class CategoryController {
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @PostMapping("/{transactionId}")
//    public ResponseEntity<CategoryResponseDTO> createCategory(@PathVariable String transactionId,
//            @RequestBody CategoryCreateDTO create) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(transactionId, create));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable String id,
//            @RequestBody CategoryUpdateDTO update) {
//        return ResponseEntity.ok(categoryService.updateCategory(id, update));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryResponseDTO> findCategoryById(@PathVariable String id) {
//        return ResponseEntity.ok(categoryService.findCategoryById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
//        return ResponseEntity.ok(categoryService.findAllCategories());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.noContent().build();
//    }
//}
