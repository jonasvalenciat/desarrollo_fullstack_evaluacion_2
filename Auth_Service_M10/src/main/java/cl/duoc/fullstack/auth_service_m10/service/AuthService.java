package cl.duoc.fullstack.auth_service_m10.service;

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

    public AuthUser register(AuthUser user) {
        log.info("Registrando nuevo usuario para autenticación: " + user.getUsername());
        AuthUser saved = authUserRepository.save(user);
        log.info("Usuario registrado exitosamente: " + saved.getUsername());
        return saved;
    }

    public String login(String username, String password) {
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        log.info("Inicio de sesión exitoso para: " + username);
        return "MOCK_JWT_TOKEN_FOR_" + username;
    }
}
