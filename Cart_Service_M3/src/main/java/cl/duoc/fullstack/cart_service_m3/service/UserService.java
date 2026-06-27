package cl.duoc.fullstack.cart_service_m3.service;

import cl.duoc.fullstack.cart_service_m3.dto.UserRequest;
import cl.duoc.fullstack.cart_service_m3.model.User;
import cl.duoc.fullstack.cart_service_m3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    public User create(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.email());
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        return repository.save(user);
    }
}
