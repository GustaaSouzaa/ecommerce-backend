package com.ecommerce.ecommercebackend.controller;

import com.ecommerce.ecommercebackend.dto.LoginRequest;
import com.ecommerce.ecommercebackend.dto.RegisterRequest;
import com.ecommerce.ecommercebackend.entity.User;
import com.ecommerce.ecommercebackend.service.UserService;
import com.ecommerce.ecommercebackend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

// NOVAS IMPORTAÇÕES NECESSÁRIAS
import com.ecommerce.ecommercebackend.dto.LoginResponse; // + NOVO IMPORT: Para o DTO de resposta do login
import org.springframework.security.core.userdetails.UserDetails; // + NOVO IMPORT: Para obter a role do Spring Security

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // Endpoint para registrar um novo usuário
    // POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Verifica se o username já existe
        if (userService.findByUsername(registerRequest.getUsername()) != null) {
            throw new BadRequestException("Nome de usuário já está em uso!");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setRole(registerRequest.getRole());

        userService.registerNewUser(newUser);

        return new ResponseEntity<>("Usuário registrado com sucesso!", HttpStatus.CREATED);
    }

    // Endpoint para fazer login de um usuário
    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtém os detalhes do usuário autenticado para pegar a role
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("ROLE_USER"); // Padrão se não encontrar

        // Retorna um LoginResponse com a mensagem, username e role
        return new ResponseEntity<>(
                new LoginResponse("Login realizado com sucesso!", userDetails.getUsername(), role),
                HttpStatus.OK
        ); // + CORRIGIDO: Adicionado o ')' que estava faltando aqui.
    }
}