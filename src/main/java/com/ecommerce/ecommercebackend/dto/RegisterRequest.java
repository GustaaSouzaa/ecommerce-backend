package com.ecommerce.ecommercebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank; // NOVO IMPORT
import jakarta.validation.constraints.Size; // NOVO IMPORT

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "O nome de usuário não pode estar em branco.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;

    @NotBlank(message = "A role (papel) não pode estar em branco.")
    private String role;
}