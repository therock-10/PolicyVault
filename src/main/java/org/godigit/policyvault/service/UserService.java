package org.godigit.policyvault.service;

import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.entities.Role;

import java.util.Set;

public interface UserService {
    Users createUser(String username, String email, String rawPassword, String department, Set<Role> roles);
    void touchLogin(String username);
}