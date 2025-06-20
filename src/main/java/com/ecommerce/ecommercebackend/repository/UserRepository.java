package com.ecommerce.ecommercebackend.repository;

import com.ecommerce.ecommercebackend.entity.User; // Importa a entidade User
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa @Repository

import java.util.Optional; // Para métodos que podem retornar ou não um usuário

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // 3. Método personalizado para buscar um usuário pelo nome de usuário
    Optional<User> findByUsername(String username);

}