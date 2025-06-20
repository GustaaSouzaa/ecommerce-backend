package com.ecommerce.ecommercebackend.service;

import com.ecommerce.ecommercebackend.entity.Cart;
import com.ecommerce.ecommercebackend.entity.CartItem;
import com.ecommerce.ecommercebackend.entity.Product;
import com.ecommerce.ecommercebackend.entity.User;
import com.ecommerce.ecommercebackend.repository.CartItemRepository;
import com.ecommerce.ecommercebackend.repository.CartRepository;
import com.ecommerce.ecommercebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.ecommercebackend.dto.CartResponse;
import com.ecommerce.ecommercebackend.dto.CartItemResponse;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // 1. Método para obter o carrinho de um usuário (ou criar um novo se não existir)
    @Transactional
    public Cart getOrCreateCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // 2. Método para adicionar ou atualizar um item no carrinho
    @Transactional // Garante que as operações de DB sejam atômicas
    public CartItem addProductToCart(User user, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        // Busca o carrinho do usuário ou cria um novo
        Cart cart = getOrCreateCartByUser(user);

        // Busca o produto pelo ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + productId));

        // Verifica o estoque
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + product.getName() + ". Disponível: " + product.getStockQuantity());
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        CartItem cartItem;

        if (existingCartItem.isPresent()) {
            // Se o item já existe, atualiza a quantidade
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Soma a nova quantidade
            // Poderíamos adicionar uma verificação de estoque aqui também, para evitar que a soma exceda o estoque
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente. Quantidade no carrinho excederia o estoque. Disponível: " + product.getStockQuantity());
            }
        } else {
            // Se o item não existe, cria um novo CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        return cartItemRepository.save(cartItem);
    }

    // 3. Método para remover um item do carrinho
    @Transactional
    public void removeProductFromCart(User user, Long productId) {
        Cart cart = getOrCreateCartByUser(user); // Busca o carrinho do usuário

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItem.isPresent()) {
            cartItemRepository.delete(existingCartItem.get()); // Deleta o item do carrinho
        } else {
            throw new RuntimeException("Produto não encontrado no carrinho.");
        }
    }

    // 4. Método para visualizar o carrinho de um usuário (convertendo para DTO de resposta)
    @Transactional // *** ESTA É A LINHA QUE ESTAVA FALTANDO E CAUSANDO O ERRO "NO SESSION" ***
    public CartResponse getCartResponseByUser(User user) {
        Cart cart = getOrCreateCartByUser(user); // Busca o carrinho do usuário

        // Este acesso a cart.getCartItems() agora ocorre dentro de uma transação ativa.
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()))
                ))
                .collect(Collectors.toList());

        // Calcula o valor total do carrinho
        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), user.getUsername(), itemResponses, totalAmount);
    }

    // 5. Método para esvaziar o carrinho (útil após finalizar compra)
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCartByUser(user);
        cartItemRepository.deleteByCart(cart); // Usa o método customizado no repositório
    }
}