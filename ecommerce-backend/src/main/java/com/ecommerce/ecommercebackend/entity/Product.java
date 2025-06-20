package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*; // Importa as anotações JPA
import lombok.Data; // Importa a anotação @Data do Lombok
import lombok.NoArgsConstructor; // Importa a anotação @NoArgsConstructor do Lombok
import lombok.AllArgsConstructor; // Importa a anotação @AllArgsConstructor do Lombok
import java.math.BigDecimal; // Para o tipo de dado de dinheiro

@Entity // 1. Marca esta classe como uma entidade JPA
@Table(name = "products") // 2. Mapeia esta entidade para a tabela "products" no banco de dados
@Data // 3. Anotação do Lombok para gerar getters, setters, toString, equals e hashCode
@NoArgsConstructor // 4. Anotação do Lombok para gerar um construtor sem argumentos
@AllArgsConstructor // 5. Anotação do Lombok para gerar um construtor com todos os argumentos
public class Product {

    @Id // 6. Marca este campo como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 7. Configura a geração automática de ID pelo banco de dados
    private Long id; // Chave primária do produto

    @Column(nullable = false, unique = true) // 8. Coluna não nula e única no banco de dados
    private String name; // Nome do produto

    @Column(columnDefinition = "TEXT") // 9. Define o tipo da coluna como TEXT (para descrições longas)
    private String description; // Descrição do produto

    @Column(nullable = false) // 10. Coluna não nula
    private BigDecimal price; // Preço do produto (BigDecimal é ideal para valores monetários)

    @Column(nullable = false) // 11. Coluna não nula
    private Integer stockQuantity; // Quantidade em estoque
}