package com.ecommerce.ecommercebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id; // ID do OrderItem
    private Long productId; // ID do produto
    private String productName; // Nome do produto
    private BigDecimal priceAtPurchase; // Preço unitário no momento da compra
    private Integer quantity; // Quantidade comprada
    private BigDecimal subtotal; // Subtotal para este item (priceAtPurchase * quantity)
}