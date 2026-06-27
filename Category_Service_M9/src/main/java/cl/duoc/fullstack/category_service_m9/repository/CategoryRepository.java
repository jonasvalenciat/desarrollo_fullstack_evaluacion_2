package cl.duoc.fullstack.category_service_m9.repository;

import cl.duoc.fullstack.category_service_m9.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
