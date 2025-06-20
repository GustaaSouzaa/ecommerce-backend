package com.ecommerce.ecommercebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id; // ID do carrinho
    private String username; // Nome de usuário do proprietário do carrinho
    private List<CartItemResponse> items; // Lista de itens no carrinho
    private BigDecimal totalAmount; // Valor total do carrinho
}