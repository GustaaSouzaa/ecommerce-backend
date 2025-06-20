package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*; // Para anotações JPA (@Entity, @Id, etc.)
import lombok.Data; // Para getters, setters, toString, etc.
import lombok.NoArgsConstructor; // Para construtor sem argumentos
import lombok.AllArgsConstructor; // Para construtor com todos os argumentos

@Entity // 1. Marca esta classe como uma entidade JPA
@Table(name = "users") // 2. Mapeia esta entidade para a tabela "users" no banco de dados
@Data // 3. Anotação do Lombok para gerar getters, setters, toString, equals e hashCode
@NoArgsConstructor // 4. Anotação do Lombok para gerar um construtor sem argumentos
@AllArgsConstructor // 5. Anotação do Lombok para gerar um construtor com todos os argumentos
public class User {

    @Id // 6. Marca este campo como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 7. Configura a geração automática de ID pelo banco de dados
    private Long id; // Chave primária do usuário

    @Column(nullable = false, unique = true, length = 50) // 8. Nome de usuário: não nulo, único, máx. 50 caracteres
    private String username; // Nome de usuário para login

    @Column(nullable = false) // 9. Senha: não nula (será armazenada criptografada)
    private String password; // Senha do usuário (criptografada)

    @Column(nullable = false) // 10. Papel/Role do usuário (ex: ROLE_USER, ROLE_ADMIN)
    private String role; // Role do usuário, ex: "ROLE_USER", "ROLE_ADMIN"
}