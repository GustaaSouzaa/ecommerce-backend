package com.ecommerce.ecommercebackend.controller;

import com.ecommerce.ecommercebackend.dto.OrderResponse;
import com.ecommerce.ecommercebackend.entity.User;
import com.ecommerce.ecommercebackend.service.OrderService;
import com.ecommerce.ecommercebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // Método auxiliar para obter o usuário logado (privado, usado internamente)
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    // 1. Endpoint para finalizar um pedido (criar um pedido a partir do carrinho)
    // POST http://localhost:8080/api/orders/checkout
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        try {
            User authenticatedUser = getAuthenticatedUser();
            OrderResponse newOrder = orderService.createOrderFromCart(authenticatedUser);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED); // Retorna 201 Created para novo recurso
        } catch (RuntimeException e) {
            // Captura exceções como "Carrinho vazio", "Estoque insuficiente"
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // 2. Endpoint para obter o histórico de pedidos do usuário logado
    // GET http://localhost:8080/api/orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        User authenticatedUser = getAuthenticatedUser();
        List<OrderResponse> orders = orderService.getOrdersByUser(authenticatedUser);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // GET http://localhost:8080/api/orders/{orderId}
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        User authenticatedUser = getAuthenticatedUser();
        List<OrderResponse> userOrders = orderService.getOrdersByUser(authenticatedUser); // Busca todos os pedidos do usuário

        // Filtra para encontrar o pedido específico pelo ID
        OrderResponse order = userOrders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado ou não pertence ao usuário."));

        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}