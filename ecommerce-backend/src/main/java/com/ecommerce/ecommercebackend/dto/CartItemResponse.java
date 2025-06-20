package com.ecommerce.ecommercebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId; // ID do produto
    private String productName; // Nome do produto
    private BigDecimal productPrice; // Preço unitário do produto
    private Integer quantity; // Quantidade deste produto no carrinho
    private BigDecimal subtotal; // Subtotal para este item (preço * quantidade)
}