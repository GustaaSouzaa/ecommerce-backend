package com.ecommerce.ecommercebackend.service;

import com.ecommerce.ecommercebackend.entity.Product; // Importa a entidade Product
import com.ecommerce.ecommercebackend.repository.ProductRepository; // Importa ProductRepository
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired
import org.springframework.stereotype.Service; // Importa @Service

import java.util.List;
import java.util.Optional; // Usado para lidar com valores que podem ou não estar presentes

@Service
public class ProductService {

    private final ProductRepository productRepository;


    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}