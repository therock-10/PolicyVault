package org.godigit.policyvault.dto;

import java.util.Set;

public class JwtResponse {
    private String token;
    private String username;
    private Set<String> roles;

    public JwtResponse(String token, String username, Set<String> roles) {
        this.token = token; this.username = username; this.roles = roles;
    }
    // getters

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Set<String> getRoles() {
        return roles;
    }
}