package com.ecommerce.ecommercebackend.repository;

import com.ecommerce.ecommercebackend.entity.Order;
import com.ecommerce.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Método para buscar todos os pedidos de um usuário específico
    List<Order> findByUser(User user);
}