package com.cms.assignment.controller;


import com.cms.assignment.appuser.User;
import com.cms.assignment.appuser.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTO for Login Request
    public static class LoginRequest {
        public String email;
        public String password;
    }

    // DTO for Login Response
    public static class LoginResponse {
        public Integer id;
        public String name;
        public String role;
        public String message;

        public LoginResponse(Integer id, String name, String role, String message) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.message = message;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.password, user.getPassword())) {
                return ResponseEntity.ok(new LoginResponse(
                        user.getId(),
                        user.getName(),
                        user.getRole().name(),
                        "Login Successful"
                ));
            }
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }

}
