package com.ecommerce.ecommercebackend.repository;

import com.ecommerce.ecommercebackend.entity.Product; // Importa a entidade Product
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa @Repository

@Repository // 1. Anotação para indicar que é um componente de repositório
public interface ProductRepository extends JpaRepository<Product, Long> {

}