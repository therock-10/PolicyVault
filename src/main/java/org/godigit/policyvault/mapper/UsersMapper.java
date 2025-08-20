package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.UsersDto;
import org.godigit.policyvault.entities.Users;

public class UsersMapper {

    public static UsersDto toDto(Users user) {
        if (user == null) {
            return null;
        }
        UsersDto dto = new UsersDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDepartment(user.getDepartment());
        dto.setRoles(user.getRoles());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setEnabled(user.isEnabled());
        return dto;
    }

    public static Users toEntity(UsersDto dto) {
        if (dto == null) {
            return null;
        }
        Users user = new Users();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setDepartment(dto.getDepartment());
        user.setRoles(dto.getRoles());
        user.setCreatedAt(dto.getCreatedAt());
        user.setLastLoginAt(dto.getLastLoginAt());
        user.setEnabled(dto.isEnabled());
        return user;
    }}