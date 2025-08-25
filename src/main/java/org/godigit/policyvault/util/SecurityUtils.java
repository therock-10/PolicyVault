package org.godigit.policyvault.util;

import jakarta.servlet.http.HttpServletRequest;
import org.godigit.policyvault.security.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtils {

    /**
     * Extracts the current user's ID from the JWT subject.
     * Assumes the JWT is passed in the Authorization header as a Bearer token.
     *
     * @param jwtService your JwtService bean to parse the token
     * @return userId as a String
     */
    public static String getCurrentUserId(JwtService jwtService) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or invalid");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return jwtService.getSubject(token); // subject = userId
    }
}
