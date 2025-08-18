package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.ChangeLogResponse;
import org.godigit.policyvault.entities.ChangeLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

    ChangeLogResponse toResponse(ChangeLog changeLog);
    ChangeLog toEntity(ChangeLogResponse changeLogResponse);

}
