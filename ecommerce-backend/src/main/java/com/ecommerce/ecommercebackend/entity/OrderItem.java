package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Muitos itens de pedido podem se referir a um único produto.
    // O `Product` é apenas uma referência para consulta; os detalhes importantes
    // como `priceAtPurchase` e `productName` são armazenados diretamente aqui
    // para manter a imutabilidade do pedido.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Coluna 'product_id' na tabela 'order_items' que referencia o 'id' da tabela 'products'
    private Product product; // O produto que foi pedido

    @Column(nullable = false)
    private Integer quantity; // Quantidade do produto neste item do pedido

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;
}