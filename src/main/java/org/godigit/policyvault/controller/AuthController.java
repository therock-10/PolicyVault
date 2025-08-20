package org.godigit.policyvault.controller;

import lombok.RequiredArgsConstructor;
import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.User;
import org.godigit.policyvault.repository.RoleRepository;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (userRepository.existsByUsername(username)) {
            return "Username already exists!";
        }
        Role role = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .build();
        userRepository.save(user);
        return "User registered!";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtUtils.generateToken(username);
    }
}