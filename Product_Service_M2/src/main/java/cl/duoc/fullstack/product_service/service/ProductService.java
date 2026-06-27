package cl.duoc.fullstack.product_service.service;

import cl.duoc.fullstack.product_service.model.Product;
import cl.duoc.fullstack.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        log.info("Obteniendo todos los productos");
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        log.info("Creando producto: {}", product.getName());
        Product saved = productRepository.save(product);
        log.info("Producto creado exitosamente con ID: {}", saved.getId());
        return saved;
    }
}
