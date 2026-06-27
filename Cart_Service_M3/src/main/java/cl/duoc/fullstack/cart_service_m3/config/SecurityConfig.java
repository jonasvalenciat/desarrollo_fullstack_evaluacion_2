package cl.duoc.fullstack.cart_service_m3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (Lectura del carrito)
                        .requestMatchers(HttpMethod.GET, "/cart", "/cart/by-id/**").permitAll()
                        // Auditoría: Exclusiva para administradores
                        .requestMatchers(HttpMethod.GET, "/cart/by-id/{id}/history").hasRole("ADMIN")
                        // Operaciones de negocio: Clientes, Agentes de Soporte o Admins
                        .requestMatchers(HttpMethod.POST, "/cart/add").hasAnyRole("USER", "AGENT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cart/by-id/**").hasAnyRole("USER", "AGENT", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/cart/by-id/**").hasAnyRole("USER", "AGENT", "ADMIN")
                        // El resto de rutas (como la gestión cruda de /users) requiere ADMIN
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}