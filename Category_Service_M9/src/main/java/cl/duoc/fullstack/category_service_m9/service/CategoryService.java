package cl.duoc.fullstack.category_service_m9.service;

import cl.duoc.fullstack.category_service_m9.dto.CategoryRequest;
import cl.duoc.fullstack.category_service_m9.dto.CategoryResponse;
import cl.duoc.fullstack.category_service_m9.model.Category;
import cl.duoc.fullstack.category_service_m9.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creando nueva categoría: {}", request.getName());
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category saved = categoryRepository.save(category);
        log.info("Categoría creada exitosamente con ID: {}", saved.getId());
        return toResponse(saved);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría no encontrada con ID: " + id));
        return toResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Actualizando categoría ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría no encontrada con ID: " + id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category saved = categoryRepository.save(category);
        log.info("Categoría actualizada exitosamente con ID: {}", saved.getId());
        return toResponse(saved);
    }

    public void deleteCategory(Long id) {
        log.info("Eliminando categoría ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Categoría no encontrada con ID: " + id);
        }
        categoryRepository.deleteById(id);
        log.info("Categoría eliminada exitosamente con ID: {}", id);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
