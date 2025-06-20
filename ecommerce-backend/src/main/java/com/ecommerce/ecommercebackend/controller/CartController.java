package com.ecommerce.ecommercebackend.controller;

import com.ecommerce.ecommercebackend.dto.CartItemRequest;
import com.ecommerce.ecommercebackend.dto.CartResponse;
import com.ecommerce.ecommercebackend.entity.User;
import com.ecommerce.ecommercebackend.service.CartService;
import com.ecommerce.ecommercebackend.service.UserService;
import com.ecommerce.ecommercebackend.exception.ResourceNotFoundException; // NOVO IMPORT
import com.ecommerce.ecommercebackend.exception.BadRequestException; // NOVO IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Importado, mas será menos usado diretamente
import jakarta.validation.Valid; // NOVO IMPORT - para ativar a validação nos DTOs

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // Método auxiliar para obter o usuário logado (privado, usado internamente)
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Verifica se o usuário está autenticado e não é anônimo
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    // Endpoint para adicionar ou atualizar um produto no carrinho
    // POST http://localhost:8080/api/cart/add
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@Valid @RequestBody CartItemRequest cartItemRequest) { // Adicionado @Valid
        // O try-catch foi removido pois as exceções (ResourceNotFoundException, BadRequestException)
        // serão capturadas pelo GlobalExceptionHandler.
        User authenticatedUser = getAuthenticatedUser();
        cartService.addProductToCart(authenticatedUser, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        // Retorna o carrinho atualizado após a adição/atualização
        CartResponse updatedCart = cartService.getCartResponseByUser(authenticatedUser);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Endpoint para remover um produto do carrinho
    // DELETE http://localhost:8080/api/cart/remove/{productId}
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable Long productId) {
        // O try-catch foi removido pelas mesmas razões acima.
        User authenticatedUser = getAuthenticatedUser();
        cartService.removeProductFromCart(authenticatedUser, productId);
        // Retorna o carrinho atualizado após a remoção
        CartResponse updatedCart = cartService.getCartResponseByUser(authenticatedUser);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Endpoint para visualizar o carrinho do usuário logado
    // GET http://localhost:8080/api/cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        User authenticatedUser = getAuthenticatedUser();
        CartResponse cart = cartService.getCartResponseByUser(authenticatedUser);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    // Endpoint para esvaziar o carrinho
    // DELETE http://localhost:8080/api/cart/clear
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        User authenticatedUser = getAuthenticatedUser();
        cartService.clearCart(authenticatedUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}