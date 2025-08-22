package org.godigit.policyvault.controller;

<<<<<<< HEAD
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
=======
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.security.JwtService;
import org.godigit.policyvault.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager; // auto-configured by Spring Boot
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository users;
    private final PasswordEncoder encoder;


    public AuthController(AuthenticationManager authManager, JwtService jwtService,
                          UserService userService, UserRepository users, PasswordEncoder encoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        Users user = (Users) users.findByUsername(req.username()).orElseThrow();
        userService.touchLogin(user.getUsername());

        var roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());
        String token = jwtService.generate(user.getUsername(), Map.of("roles", roles));
        return ResponseEntity.ok(new JwtResponse(token, user.getUsername(), roles));
    }

    // Admin-only provisioning endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (users.existsByUsername(req.username())) return ResponseEntity.badRequest().body("Username taken");
        if (users.existsByEmail(req.email())) return ResponseEntity.badRequest().body("Email taken");
        // You can lock this route with @PreAuthorize("hasRole('ADMIN')") if exposed beyond bootstrap
        Users u = userService.createUser(req.username(), req.email(), req.password(),
                req.department(), req.roles() == null ? Set.of(Role.ROLE_EMPLOYEE) : req.roles());
        return ResponseEntity.ok(Map.of("id", u.getId(), "username", u.getUsername()));
    }
}
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
