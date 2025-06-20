package com.ecommerce.ecommercebackend.service;

import com.ecommerce.ecommercebackend.entity.User; // Importa a entidade User
import com.ecommerce.ecommercebackend.repository.UserRepository; // Importa UserRepository
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder
import org.springframework.stereotype.Service; // Importa @Service

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerNewUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null); // Retorna null se não encontrar
    }
}