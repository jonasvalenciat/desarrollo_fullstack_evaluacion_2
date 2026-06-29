package cl.duoc.fullstack.auth_service_m10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterResponse {
    private Long id;
    private String username;
    private String role;
}
