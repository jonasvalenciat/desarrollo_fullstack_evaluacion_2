package cl.duoc.fullstack.cart_service_m3.config;

import cl.duoc.fullstack.cart_service_m3.model.User;
import cl.duoc.fullstack.cart_service_m3.model.User.Role;
import cl.duoc.fullstack.cart_service_m3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("h2")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@empresa.com");
            admin.setPassword(passwordEncoder.encode("pass123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            User ana = new User();
            ana.setName("Ana Garcia");
            ana.setEmail("ana.garcia@empresa.com");
            ana.setPassword(passwordEncoder.encode("user123"));
            ana.setRole(Role.USER);
            ana.setActive(true);
            userRepository.save(ana);

            User carlos = new User();
            carlos.setName("Carlos Lopez");
            carlos.setEmail("carlos.lopez@empresa.com");
            carlos.setPassword(passwordEncoder.encode("user123"));
            carlos.setRole(Role.AGENT);
            carlos.setActive(true);
            userRepository.save(carlos);
        }
    }
}