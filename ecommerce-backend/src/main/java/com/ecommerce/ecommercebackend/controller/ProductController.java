package com.ecommerce.ecommercebackend.controller;

import com.ecommerce.ecommercebackend.entity.Product;
import com.ecommerce.ecommercebackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Endpoint para obter todos os produtos
    // GET http://localhost:8080/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK); // Retorna 200 OK
    }

    // Endpoint para obter um produto por ID
    // GET http://localhost:8080/api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        // Verifica se o produto foi encontrado
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Se encontrou, retorna 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Se não, retorna 404 Not Found
    }

    // Endpoint para criar um novo produto
    // POST http://localhost:8080/api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED); // Retorna 201 Created
    }

    // Endpoint para atualizar um produto existente
    // PUT http://localhost:8080/api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Primeiro, verifica se o produto existe
        return productService.getProductById(id).map(existingProduct -> {
            // Atualiza os dados do produto existente com os novos dados
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStockQuantity(product.getStockQuantity());

            Product updatedProduct = productService.saveProduct(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK); // Retorna 200 OK
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Se não encontrou, retorna 404 Not Found
    }

    // Endpoint para deletar um produto por ID
    // DELETE http://localhost:8080/api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Primeiro, verifica se o produto existe antes de tentar deletar
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content (sucesso sem corpo de resposta)
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Se não encontrou, retorna 404 Not Found
        }
    }
}