package cl.duoc.fullstack.cart_service_m3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    @Column(length = 20)
    private String previousStatus;

    @Column(length = 20)
    private String newStatus;

    @Column(length = 150)
    private String previousUserEmail;

    @Column(length = 150)
    private String newUserEmail;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(length = 255)
    private String comment;
}
