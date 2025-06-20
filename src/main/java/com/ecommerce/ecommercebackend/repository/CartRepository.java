package com.ecommerce.ecommercebackend.repository;

import com.ecommerce.ecommercebackend.entity.Cart;
import com.ecommerce.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Método personalizado para buscar um carrinho pelo usuário.
    // Um usuário deve ter apenas um carrinho.
    Optional<Cart> findByUser(User user);
}