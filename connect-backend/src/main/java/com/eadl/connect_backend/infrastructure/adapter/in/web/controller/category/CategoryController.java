package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eadl.connect_backend.application.dto.CategoryDto;
import com.eadl.connect_backend.application.mapper.CategoryMapper;
import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.in.category.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // ================== PUBLIC ==================

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories()
                        .stream()
                        .map(categoryMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryDto>> getActiveCategories() {
        return ResponseEntity.ok(
                categoryService.getActiveCategories()
                        .stream()
                        .map(categoryMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/technicians/count")
    public ResponseEntity<Long> countTechnicians(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.countTechniciansByCategory(id));
    }

    // ================== ADMIN ==================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto dto
    ) {
        Category category = categoryMapper.toModel(dto);
        Category created = categoryService.createCategory(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryMapper.toDto(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        Category updated = categoryService.updateCategory(id, categoryMapper.toModel(dto));
        return ResponseEntity.ok(categoryMapper.toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/icon")
    public ResponseEntity<CategoryDto> updateIcon(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(
                categoryMapper.toDto(
                        categoryService.updateCategoryIcon(id, dto.getIconUrl())
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/order")
    public ResponseEntity<CategoryDto> updateDisplayOrder(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(
                categoryMapper.toDto(
                        categoryService.updateDisplayOrder(id, dto.getDisplayOrder())
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        categoryService.activateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}