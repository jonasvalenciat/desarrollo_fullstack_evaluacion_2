package cl.duoc.fullstack.category_service_m9.service;

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

    public Category createCategory(Category category) {
        log.info("Creando nueva categoría: " + category.getName());
        Category saved = categoryRepository.save(category);
        log.info("Categoría creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría no encontrada con ID: " + id));
    }
}
