package org.godigit.policyvault.security;

<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.godigit.policyvault.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
=======
import org.godigit.policyvault.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
<<<<<<< HEAD
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/policies/**").hasAnyRole("EMPLOYEE","DEPARTMENT_HEAD","COMPLIANCE_OFFICER","ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
=======
@EnableMethodSecurity // enables @PreAuthorize on services/controllers
public class SecurityConfig {

    private final CustomUserDetailsService uds;
    private final JwtService jwtService;

    public SecurityConfig(CustomUserDetailsService uds, JwtService jwtService) {
        this.uds = uds;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        // Ensure ROLE_ prefix is kept as-is
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        provider.setAuthoritiesMapper(mapper);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthenticationFilter(jwtService, uds);

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // public read/search for employees
                        .requestMatchers(HttpMethod.GET, "/api/policies/**").hasAnyRole(
                                "EMPLOYEE","DEPT_HEAD","COMPLIANCE_OFFICER","ADMIN")

                        // create/update/delete policies
                        .requestMatchers(HttpMethod.POST, "/api/policies/**").hasAnyRole(
                                "COMPLIANCE_OFFICER","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/policies/**").hasAnyRole(
                                "COMPLIANCE_OFFICER","ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/policies/**").hasAnyRole(
                                "COMPLIANCE_OFFICER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/policies/**").hasRole("ADMIN")

                        // user admin
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
    }
}
