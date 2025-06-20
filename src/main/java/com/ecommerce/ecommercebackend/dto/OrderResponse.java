package com.ecommerce.ecommercebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id; // ID do pedido
    private String username; // Nome de usuário do cliente que fez o pedido
    private LocalDateTime orderDate; // Data e hora do pedido
    private BigDecimal totalAmount; // Valor total do pedido
    private List<OrderItemResponse> items; // Lista de itens deste pedido
}