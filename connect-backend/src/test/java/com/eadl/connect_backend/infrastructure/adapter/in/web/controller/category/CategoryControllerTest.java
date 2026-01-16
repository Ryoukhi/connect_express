package com.eadl.connect_backend.infrastructure.adapter.in.web.controller.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eadl.connect_backend.application.dto.CategoryDto;
import com.eadl.connect_backend.application.mapper.CategoryMapper;
import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.in.category.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        // Arrange
        Category category = new Category();
        CategoryDto dto = new CategoryDto();
        dto.setName("Test Category");

        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"));
    }

    @Test
    void getActiveCategories_ShouldReturnListOfActiveCategories() throws Exception {
        // Arrange
        Category category = new Category();
        CategoryDto dto = new CategoryDto();
        dto.setName("Active Category");

        when(categoryService.getActiveCategories()).thenReturn(List.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/categories/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Active Category"));
    }

    @Test
    void getById_ShouldReturnCategory_WhenFound() throws Exception {
        // Arrange
        Long id = 1L;
        Category category = new Category();
        CategoryDto dto = new CategoryDto();
        dto.setIdCategory(id);

        when(categoryService.getCategoryById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategory").value(id));
    }

    // ...
    @Test
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("New Category");
        Category category = new Category();
        CategoryDto outputDto = new CategoryDto();
        outputDto.setIdCategory(1L);
        outputDto.setName("New Category");

        when(categoryMapper.toModel(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    // ...
    @Test
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        // Arrange
        Long id = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Updated Name");
        Category category = new Category();
        CategoryDto outputDto = new CategoryDto();
        outputDto.setIdCategory(id);
        outputDto.setName("Updated Name");

        when(categoryMapper.toModel(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.updateCategory(eq(id), any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void updateIcon_ShouldReturnCategoryWithNewIcon() throws Exception {
        // Arrange
        Long id = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setIconUrl("http://new-icon.com");
        Category category = new Category();
        CategoryDto outputDto = new CategoryDto();
        outputDto.setIconUrl("http://new-icon.com");

        when(categoryService.updateCategoryIcon(id, "http://new-icon.com")).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(patch("/api/categories/{id}/icon", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iconUrl").value("http://new-icon.com"));
    }

    @Test
    void updateDisplayOrder_ShouldReturnCategoryWithNewOrder() throws Exception {
        // Arrange
        Long id = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setDisplayOrder(10);
        Category category = new Category();
        CategoryDto outputDto = new CategoryDto();
        outputDto.setDisplayOrder(10);

        when(categoryService.updateDisplayOrder(id, 10)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(patch("/api/categories/{id}/order", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayOrder").value(10));
    }

    @Test
    void activate_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        when(categoryService.activateCategory(id)).thenReturn(new Category());

        // Act & Assert
        mockMvc.perform(patch("/api/categories/{id}/activate", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deactivate_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        when(categoryService.deactivateCategory(id)).thenReturn(new Category());

        // Act & Assert
        mockMvc.perform(patch("/api/categories/{id}/deactivate", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(categoryService).deleteCategory(id);

        // Act & Assert
        mockMvc.perform(delete("/api/categories/{id}", id))
                .andExpect(status().isNoContent());
    }
}
