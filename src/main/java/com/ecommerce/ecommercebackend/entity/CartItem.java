package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos itens de carrinho podem pertencer a um único carrinho.
    // `@ManyToOne`: Relacionamento muitos-para-um.
    // `fetch = FetchType.LAZY`: Carrega o Cart apenas quando necessário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Muitos itens de carrinho podem se referir a um único produto.
    // `@ManyToOne`: Relacionamento muitos-para-um.
    // `fetch = FetchType.LAZY`: Carrega o Product apenas quando necessário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Coluna 'product_id' na tabela 'cart_items' que referencia o 'id' da tabela 'products'
    private Product product; // O produto no carrinho

    @Column(nullable = false)
    private Integer quantity;
}