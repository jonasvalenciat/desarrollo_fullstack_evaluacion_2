package cl.duoc.fullstack.cart_service_m3.controller;

import cl.duoc.fullstack.cart_service_m3.dto.AssignUserRequest;
import cl.duoc.fullstack.cart_service_m3.dto.CartHistoryResult;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemCommand;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemRequest;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemResponse;
import cl.duoc.fullstack.cart_service_m3.dto.CartItemResult;
import cl.duoc.fullstack.cart_service_m3.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class Controller_Cart_Service {

    private final CartService service;

    public Controller_Cart_Service(CartService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getAllItems(@RequestParam(required = false) String status) {
        List<CartItemResult> results = (status != null)
                ? this.service.getCartContent(status)
                : this.service.getCartContent();
        List<CartItemResponse> responses = results.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CartItemResponse> getById(@PathVariable Long id) {
        return this.service.getById(id)
                .map(result -> ResponseEntity.ok(toResponse(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addItem(@Valid @RequestBody CartItemRequest request) {
        CartItemResult result = this.service.addToCart(toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PutMapping("/by-id/{id}")
    @PreAuthorize("@cartSecurity.canEdit(#id, authentication)")
    public ResponseEntity<CartItemResponse> updateById(@PathVariable Long id, @Valid @RequestBody CartItemRequest request) {
        return this.service.updateById(id, toCommand(request))
                .map(result -> ResponseEntity.ok(toResponse(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/by-id/{id}/assign")
    @PreAuthorize("@cartSecurity.canEdit(#id, authentication)")
    public ResponseEntity<CartItemResponse> assignUser(@PathVariable Long id, @Valid @RequestBody AssignUserRequest request) {
        return this.service.assignUser(id, request.userEmail())
                .map(result -> ResponseEntity.ok(toResponse(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/by-id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (this.service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-id/{id}/history")
    public ResponseEntity<List<CartHistoryResult>> getHistory(@PathVariable Long id) {
        return this.service.getHistory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CartItemCommand toCommand(CartItemRequest request) {
        return new CartItemCommand(
                request.productName(),
                request.price(),
                request.quantity(),
                request.userEmail(),
                request.couponCode()
        );
    }

    private CartItemResponse toResponse(CartItemResult result) {
        return new CartItemResponse(
                result.id(),
                result.productName(),
                result.price(),
                result.quantity(),
                result.couponCode(),
                result.status(),
                result.user()
        );
    }
}
