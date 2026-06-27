package cl.duoc.fullstack.auth_service_m10.controller;

import cl.duoc.fullstack.auth_service_m10.model.AuthUser;
import cl.duoc.fullstack.auth_service_m10.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@Valid @RequestBody AuthUser user) {
        AuthUser created = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthUser credentials) {
        String token = authService.login(credentials.getUsername(), credentials.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
