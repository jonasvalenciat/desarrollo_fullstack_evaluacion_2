package cl.duoc.fullstack.category_service_m9;

import cl.duoc.fullstack.category_service_m9.dto.CategoryRequest;
import cl.duoc.fullstack.category_service_m9.dto.CategoryResponse;
import cl.duoc.fullstack.category_service_m9.model.Category;
import cl.duoc.fullstack.category_service_m9.repository.CategoryRepository;
import cl.duoc.fullstack.category_service_m9.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void createCategory_ShouldReturnCategoryResponse() {
        // Given
        CategoryRequest request = new CategoryRequest("Electrónicos", "Productos electrónicos");
        Category savedCategory = new Category(1L, "Electrónicos", "Productos electrónicos");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // When
        CategoryResponse response = categoryService.createCategory(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electrónicos", response.getName());
        assertEquals("Productos electrónicos", response.getDescription());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        // Given
        List<Category> categories = List.of(
                new Category(1L, "Electrónicos", "Productos electrónicos"),
                new Category(2L, "Ropa", "Prendas de vestir")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<CategoryResponse> responses = categoryService.getAllCategories();

        // Then
        assertEquals(2, responses.size());
        assertEquals("Electrónicos", responses.get(0).getName());
        assertEquals("Ropa", responses.get(1).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_WhenExists_ShouldReturnCategory() {
        // Given
        Category category = new Category(1L, "Electrónicos", "Productos electrónicos");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When
        CategoryResponse response = categoryService.getCategoryById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electrónicos", response.getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_WhenNotExists_ShouldThrowException() {
        // Given
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryById(99L));
        assertTrue(exception.getMessage().contains("99"));
        verify(categoryRepository).findById(99L);
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnResponse() {
        // Given
        CategoryRequest request = new CategoryRequest("Electrodomésticos", "Electrodomésticos para el hogar");
        Category existingCategory = new Category(1L, "Electrónicos", "Productos electrónicos");
        Category updatedCategory = new Category(1L, "Electrodomésticos", "Electrodomésticos para el hogar");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        CategoryResponse response = categoryService.updateCategory(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electrodomésticos", response.getName());
        assertEquals("Electrodomésticos para el hogar", response.getDescription());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void deleteCategory_WhenExists_ShouldDelete() {
        // Given
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // When
        categoryService.deleteCategory(1L);

        // Then
        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_WhenNotExists_ShouldThrowException() {
        // Given
        when(categoryRepository.existsById(99L)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteCategory(99L));
        assertTrue(exception.getMessage().contains("99"));
        verify(categoryRepository).existsById(99L);
        verify(categoryRepository, never()).deleteById(any());
    }
}
