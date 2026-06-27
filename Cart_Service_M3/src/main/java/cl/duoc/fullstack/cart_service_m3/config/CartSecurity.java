package cl.duoc.fullstack.cart_service_m3.config;

import cl.duoc.fullstack.cart_service_m3.model.CartItem;
import cl.duoc.fullstack.cart_service_m3.repository.CartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("cartSecurity")
public class CartSecurity {

    private final CartRepository cartItemRepository;

    public CartSecurity(CartRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public boolean canEdit(Long itemId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasRole(authentication, "ROLE_ADMIN")) {
            return true;
        }

        String email = authentication.getName();

        return cartItemRepository.findById(itemId)
                .map(item -> {
                    if (hasRole(authentication, "ROLE_USER")) {
                        return item.getUser() != null && email.equals(item.getUser().getEmail());
                    }
                    if (hasRole(authentication, "ROLE_AGENT")) {
                        return item.getUser() != null && email.equals(item.getUser().getEmail());
                    }
                    return false;
                })
                .orElse(false);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}