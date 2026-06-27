package cl.duoc.fullstack.cart_service_m3.service;

import cl.duoc.fullstack.cart_service_m3.client.NotificationClient;
import cl.duoc.fullstack.cart_service_m3.dto.CartHistoryResult;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemCommand;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemResult;
import cl.duoc.fullstack.cart_service_m3.dto.UserResult;
import cl.duoc.fullstack.cart_service_m3.exception.BadRequestException;
import cl.duoc.fullstack.cart_service_m3.model.CartHistory;
import cl.duoc.fullstack.cart_service_m3.model.CartItem;
import cl.duoc.fullstack.cart_service_m3.model.User;
import cl.duoc.fullstack.cart_service_m3.repository.CartHistoryRepository;
import cl.duoc.fullstack.cart_service_m3.repository.CartRepository;
import cl.duoc.fullstack.cart_service_m3.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartService {

    private final CartRepository repository;
    private final UserRepository userRepository;
    private final CartHistoryRepository historyRepository;
    private final NotificationClient notificationClient;

    public CartService(CartRepository repository, UserRepository userRepository, CartHistoryRepository historyRepository, NotificationClient notificationClient) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.notificationClient = notificationClient;
    }

    public List<CartItemResult> getCartContent() {
        return repository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public List<CartItemResult> getCartContent(String status) {
        return repository.findByStatusIgnoreCase(status).stream()
                .map(this::toResult)
                .toList();
    }

    public CartItemResult addToCart(CartItemCommand command) {
        log.info("Procesando solicitud para agregar producto: '{}' al carrito", command.productName());
        if (repository.existsByProductNameIgnoreCase(command.productName())) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: \"" + command.productName() + "\" en el carrito");
        }
        User user = userRepository.findByEmail(command.userEmail())
                .orElseThrow(() -> new BadRequestException("No existe un usuario con el email: " + command.userEmail()));
        if (command.couponCode() != null && command.couponCode().equals(user.getEmail())) {
            throw new IllegalArgumentException("El código de cupón no puede ser igual al email del usuario");
        }
        CartItem item = new CartItem();
        item.setProductName(command.productName());
        item.setPrice(command.price());
        item.setQuantity(command.quantity());
        item.setUser(user);
        item.setCouponCode(command.couponCode());
        item.setStatus("PENDING");
        CartItem saved = repository.save(item);
        recordChange(saved, null, "PENDING", null, user.getEmail(), "Producto agregado al carrito");
        if (saved.getUser() != null) {
            this.notificationClient.sendNotification(
                    "Producto Detectado",
                    "Se agregó '" + saved.getProductName() + "' a tu carrito de compras con éxito.",
                    "INFO",
                    saved.getUser().getEmail()
            );
        }
        log.info("Producto agregado exitosamente al carrito: ID={}, Producto='{}'", saved.getId(), saved.getProductName());
        return toResult(saved);
    }

    public Optional<CartItemResult> getById(Long id) {
        return repository.findById(id)
                .map(this::toResult);
    }

    public Optional<CartItemResult> updateById(Long id, CartItemCommand command) {
        Optional<CartItem> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        CartItem item = existing.get();
        String prevStatus = item.getStatus();
        String prevEmail = item.getUser() != null ? item.getUser().getEmail() : null;

        if (command.couponCode() != null && command.couponCode().equals(item.getUser().getEmail())) {
            throw new IllegalArgumentException("El código de cupón no puede ser igual al email del usuario");
        }
        User user = userRepository.findByEmail(command.userEmail())
                .orElseThrow(() -> new BadRequestException("No existe un usuario con el email: " + command.userEmail()));
        item.setProductName(command.productName());
        item.setPrice(command.price());
        item.setQuantity(command.quantity());
        item.setUser(user);
        item.setCouponCode(command.couponCode());
        CartItem saved = repository.save(item);
        recordChange(saved, prevStatus, saved.getStatus(), prevEmail, user.getEmail(), "Producto actualizado");
        return Optional.of(toResult(saved));
    }

    public Optional<CartItemResult> assignUser(Long id, String email) {
        Optional<CartItem> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        CartItem item = existing.get();
        String prevEmail = item.getUser() != null ? item.getUser().getEmail() : null;
        String newEmail;

        if (email == null || email.isBlank()) {
            item.setUser(null);
            newEmail = null;
        } else {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("No existe un usuario con el email: " + email));
            item.setUser(user);
            newEmail = user.getEmail();
        }
        CartItem saved = repository.save(item);
        recordChange(saved, saved.getStatus(), saved.getStatus(), prevEmail, newEmail, "Usuario asignado al carrito");
        return Optional.of(toResult(saved));
    }

    public Optional<List<CartHistoryResult>> getHistory(Long cartItemId) {
        if (!repository.existsById(cartItemId)) {
            return Optional.empty();
        }
        List<CartHistoryResult> history = historyRepository.findByCartItemIdOrderByChangedAtDesc(cartItemId)
                .stream()
                .map(h -> new CartHistoryResult(
                        h.getId(),
                        h.getPreviousStatus(),
                        h.getNewStatus(),
                        h.getPreviousUserEmail(),
                        h.getNewUserEmail(),
                        h.getChangedAt(),
                        h.getComment()
                ))
                .toList();
        return Optional.of(history);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private void recordChange(CartItem item, String prevStatus, String newStatus, String prevEmail, String newEmail, String comment) {
        if (prevStatus == null && newStatus == null && prevEmail == null && newEmail == null) {
            return;
        }
        boolean sameStatus = prevStatus == null ? newStatus == null : prevStatus.equals(newStatus);
        boolean sameEmail = prevEmail == null ? newEmail == null : prevEmail.equals(newEmail);
        if (sameStatus && sameEmail) {
            return;
        }
        CartHistory entry = new CartHistory();
        entry.setCartItem(item);
        entry.setPreviousStatus(prevStatus);
        entry.setNewStatus(newStatus);
        entry.setPreviousUserEmail(prevEmail);
        entry.setNewUserEmail(newEmail);
        entry.setChangedAt(LocalDateTime.now());
        entry.setComment(comment);
        historyRepository.save(entry);
    }

    private CartItemResult toResult(CartItem item) {
        UserResult userResult = null;
        if (item.getUser() != null) {
            userResult = new UserResult(
                    item.getUser().getId(),
                    item.getUser().getName(),
                    item.getUser().getEmail()
            );
        }
        return new CartItemResult(
                item.getId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getCouponCode(),
                item.getStatus(),
                userResult
        );
    }
}
