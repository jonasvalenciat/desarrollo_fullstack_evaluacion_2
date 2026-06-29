package cl.duoc.fullstack.auth_service_m10;

import cl.duoc.fullstack.auth_service_m10.dto.AuthRegisterRequest;
import cl.duoc.fullstack.auth_service_m10.dto.AuthRegisterResponse;
import cl.duoc.fullstack.auth_service_m10.model.AuthUser;
import cl.duoc.fullstack.auth_service_m10.repository.AuthUserRepository;
import cl.duoc.fullstack.auth_service_m10.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authUserRepository);
    }

    @Test
    void register_ShouldReturnResponseWithoutPassword() {
        // Given
        AuthRegisterRequest request = new AuthRegisterRequest("usuario1", "pass123");
        AuthUser savedUser = new AuthUser(1L, "usuario1", "pass123", "ROLE_USER");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(savedUser);

        // When
        AuthRegisterResponse response = authService.register(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("usuario1", response.getUsername());
        assertEquals("ROLE_USER", response.getRole());
        verify(authUserRepository).save(any(AuthUser.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        // Given
        AuthUser user = new AuthUser(1L, "usuario1", "pass123", "ROLE_USER");
        when(authUserRepository.findByUsername("usuario1")).thenReturn(Optional.of(user));

        // When
        String token = authService.login("usuario1", "pass123");

        // Then
        assertNotNull(token);
        assertTrue(token.startsWith("MOCK_JWT_TOKEN_FOR_"));
        assertTrue(token.contains("usuario1"));
        verify(authUserRepository).findByUsername("usuario1");
    }

    @Test
    void login_WithInvalidUsername_ShouldThrowException() {
        // Given
        when(authUserRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.login("unknown", "pass123"));
        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(authUserRepository).findByUsername("unknown");
    }

    @Test
    void login_WithWrongPassword_ShouldThrowException() {
        // Given
        AuthUser user = new AuthUser(1L, "usuario1", "pass123", "ROLE_USER");
        when(authUserRepository.findByUsername("usuario1")).thenReturn(Optional.of(user));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.login("usuario1", "wrongpass"));
        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(authUserRepository).findByUsername("usuario1");
    }
}
