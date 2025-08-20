package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.UsersDto;
import org.godigit.policyvault.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersDto toDto(Users user);

    Users toEntity(UsersDto dto);
}
