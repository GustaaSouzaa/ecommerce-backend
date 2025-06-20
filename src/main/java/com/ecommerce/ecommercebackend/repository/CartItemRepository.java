package com.ecommerce.ecommercebackend.repository;

import com.ecommerce.ecommercebackend.entity.Cart;
import com.ecommerce.ecommercebackend.entity.CartItem;
import com.ecommerce.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Método para buscar um CartItem específico dentro de um Cart para um determinado Product
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Método para buscar todos os CartItems de um determinado Cart
    List<CartItem> findByCart(Cart cart);

    // Método para deletar todos os CartItems de um determinado Cart
    void deleteByCart(Cart cart);
}