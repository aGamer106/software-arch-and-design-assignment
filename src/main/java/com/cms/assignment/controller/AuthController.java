package com.cms.assignment.controller;


import com.cms.assignment.Role;
import com.cms.assignment.appuser.User;
import com.cms.assignment.appuser.UserRepository;
import com.cms.assignment.helpdeskagent.HelpDeskAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class LoginResponse {
        public Integer id;
        public String name;
        public String email;
        public String role;
        public String message;

        public LoginResponse(Integer id, String name, String email, String role, String message) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.message = message;
        }
    }

    public static class AgentRegistrationRequest {
        public String name;
        public String email;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.email, request.password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, httpRequest, httpResponse);
            User user = userRepository.findByEmail(request.email).orElseThrow();
            return ResponseEntity.ok(new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name(),
                    "Login Successful"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/register-agent")
    public ResponseEntity<?> registerAgent(@RequestBody AgentRegistrationRequest request) {
        try {
            if (userRepository.findByEmail(request.email).isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            HelpDeskAgent newAgent = new HelpDeskAgent();
            newAgent.setName(request.name);
            newAgent.setEmail(request.email);
            newAgent.setPassword(passwordEncoder.encode(request.password));
            newAgent.setRole(Role.HELP_DESK_AGENT);

            newAgent.setEmployeeId("HDA-" + System.currentTimeMillis());
            newAgent.setDepartment("General Support");

            userRepository.save(newAgent);

            return ResponseEntity.ok("Agent registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating agent");
        }
    }

}
