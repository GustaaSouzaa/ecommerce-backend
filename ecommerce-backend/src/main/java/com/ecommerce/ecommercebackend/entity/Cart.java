package com.ecommerce.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um usuário tem um carrinho, e um carrinho pertence a um usuário.
    // `@OneToOne`: Relacionamento um-para-um.
    // `cascade = CascadeType.ALL`: Operações no Cart (ex: deletar) cascatearão para o User associado.
    // `fetch = FetchType.LAZY`: Carrega o User apenas quando necessário (otimização).
    // `orphanRemoval = true`: Se um User for desassociado do Cart, ele será removido do BD.

    @OneToOne(fetch = FetchType.LAZY) // Remova o 'cascade = CascadeType.ALL, orphanRemoval = true'
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Um carrinho pode ter vários itens (CartItem).
    // `@OneToMany`: Relacionamento um-para-muitos.
    // `mappedBy = "cart"`: Indica que o campo 'cart' na entidade 'CartItem' é quem faz o mapeamento.
    // `cascade = CascadeType.ALL`: Operações no Cart (ex: deletar) cascatearão para os CartItems.
    // `orphanRemoval = true`: Remove CartItems do BD se forem desassociados do Cart.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>(); // Itens dentro do carrinho
}