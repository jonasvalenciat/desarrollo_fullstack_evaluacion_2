package cl.duoc.fullstack.auth_service_m10.service;

import cl.duoc.fullstack.auth_service_m10.dto.AuthRegisterRequest;
import cl.duoc.fullstack.auth_service_m10.dto.AuthRegisterResponse;
import cl.duoc.fullstack.auth_service_m10.model.AuthUser;
import cl.duoc.fullstack.auth_service_m10.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;

    public AuthRegisterResponse register(AuthRegisterRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        AuthUser user = new AuthUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        AuthUser saved = authUserRepository.save(user);
        log.info("Usuario registrado exitosamente: {}", saved.getUsername());
        return new AuthRegisterResponse(saved.getId(), saved.getUsername(), saved.getRole());
    }

    public String login(String username, String password) {
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        log.info("Inicio de sesión exitoso para: {}", username);
        return "MOCK_JWT_TOKEN_FOR_" + username;
    }
}
