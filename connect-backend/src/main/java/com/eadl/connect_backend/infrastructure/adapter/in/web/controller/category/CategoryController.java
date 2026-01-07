package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eadl.connect_backend.application.dto.CategoryDto;
import com.eadl.connect_backend.application.mapper.CategoryMapper;
import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.in.category.CategoryService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints pour la gestion des catégories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // ================== PUBLIC ==================

    @Operation(summary = "Récupère toutes les catégories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des catégories",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories().stream().map(categoryMapper::toDto).toList()
        );
    }

    @Operation(summary = "Récupère toutes les catégories actives")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des catégories actives",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)))
    })
    @GetMapping("/active")
    public ResponseEntity<List<CategoryDto>> getActiveCategories() {
        return ResponseEntity.ok(
                categoryService.getActiveCategories().stream().map(categoryMapper::toDto).toList()
        );
    }

    @Operation(summary = "Récupère une catégorie par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id
    ) {
        return categoryService.getCategoryById(id)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Compte le nombre de techniciens d'une catégorie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre de techniciens")
    })
    @GetMapping("/{id}/technicians/count")
    public ResponseEntity<Long> countTechnicians(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.countTechniciansByCategory(id));
    }

    // ================== ADMIN ==================

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crée une nouvelle catégorie (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Catégorie créée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        Category category = categoryMapper.toModel(dto);
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toDto(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Met à jour une catégorie existante (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catégorie mise à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        Category updated = categoryService.updateCategory(id, categoryMapper.toModel(dto));
        return ResponseEntity.ok(categoryMapper.toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Met à jour l'icône d'une catégorie (admin)")
    @PatchMapping("/{id}/icon")
    public ResponseEntity<CategoryDto> updateIcon(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(
                categoryMapper.toDto(categoryService.updateCategoryIcon(id, dto.getIconUrl()))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Met à jour l'ordre d'affichage d'une catégorie (admin)")
    @PatchMapping("/{id}/order")
    public ResponseEntity<CategoryDto> updateDisplayOrder(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(
                categoryMapper.toDto(categoryService.updateDisplayOrder(id, dto.getDisplayOrder()))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Active une catégorie (admin)")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id
    ) {
        categoryService.activateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Désactive une catégorie (admin)")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id
    ) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprime une catégorie (admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Catégorie supprimée"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la catégorie") @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
