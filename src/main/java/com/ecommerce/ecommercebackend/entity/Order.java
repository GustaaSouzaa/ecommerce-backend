package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária do pedido

    // Um pedido pertence a um usuário.
    // `@ManyToOne`: Muitos pedidos para um usuário.
    // `fetch = FetchType.LAZY`: Carrega o User apenas quando necessário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Coluna 'user_id' na tabela 'orders' que referencia o 'id' da tabela 'users'
    private User user; // O usuário que fez este pedido

    @CreationTimestamp // 1. Gera automaticamente o timestamp de criação
    @Column(nullable = false, updatable = false) // Não nulo e não atualizável após a criação
    private LocalDateTime orderDate; // Data e hora em que o pedido foi feito

    @Column(nullable = false)
    private BigDecimal totalAmount; // Valor total do pedido

    // Um pedido pode ter vários itens (OrderItem).
    // `@OneToMany`: Relacionamento um-para-muitos.
    // `mappedBy = "order"`: Indica que o campo 'order' na entidade 'OrderItem' é quem faz o mapeamento.
    // `cascade = CascadeType.ALL`: Operações no Order (ex: deletar) cascatearão para os OrderItems.
    // `orphanRemoval = true`: Remove OrderItems do BD se forem desassociados do Order.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // Itens dentro do pedido
}