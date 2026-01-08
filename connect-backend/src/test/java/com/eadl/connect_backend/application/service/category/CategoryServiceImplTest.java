package com.eadl.connect_backend.application.service.category;

import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TechnicianSkillRepository technicianSkillRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.create("Plomberie", "Travaux de plomberie");
    }

    // ================= CREATE =================

    @Test
    void shouldCreateCategorySuccessfully() {
        when(categoryRepository.existsByName("Plomberie")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Plomberie");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionIfCategoryNameIsEmpty() {
        Category invalid = Category.create("", "desc");

        assertThatThrownBy(() -> categoryService.createCategory(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nom de la catégorie");
    }

    @Test
    void shouldThrowExceptionIfCategoryAlreadyExists() {
        when(categoryRepository.existsByName("Plomberie")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(category))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe déjà");
    }

    // ================= READ =================

    @Test
    void shouldReturnCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnAllCategoriesOrdered() {
        when(categoryRepository.findAllOrderedByDisplayOrder())
                .thenReturn(List.of(category));

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).hasSize(1);
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateCategoryIcon() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category updated = categoryService.updateCategoryIcon(1L, "icon.png");

        assertThat(updated.getIconUrl()).isEqualTo("icon.png");
    }

    @Test
    void shouldUpdateDisplayOrder() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category updated = categoryService.updateDisplayOrder(1L, 2);

        assertThat(updated.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    void shouldThrowIfUpdateOnUnknownCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                categoryService.updateCategoryIcon(1L, "icon.png"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ================= ACTIVATE / DEACTIVATE =================

    @Test
    void shouldDeactivateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);

        Category result = categoryService.deactivateCategory(1L);

        assertThat(result.isActive()).isFalse();
    }

    @Test
    void shouldActivateCategory() {
        category.deactivate();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);

        Category result = categoryService.activateCategory(1L);

        assertThat(result.isActive()).isTrue();
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteCategoryIfNoTechniciansAttached() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(technicianSkillRepository.findByCategoryId(1L)).thenReturn(List.of());

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldNotDeleteCategoryIfTechniciansExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(technicianSkillRepository.findByCategoryId(1L))
                .thenReturn(List.of(mock(TechnicianSkill.class)));


        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Impossible de supprimer");
    }
}
