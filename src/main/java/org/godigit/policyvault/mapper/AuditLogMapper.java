package org.godigit.policyvault.mapper;


import org.godigit.policyvault.dto.AuditLogDto;
import org.godigit.policyvault.entities.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogDto toDto(AuditLog auditLog);
    AuditLog toEntity(AuditLogDto auditLogDto);
}
