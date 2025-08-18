package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.PolicyVersionResponse;
import org.godigit.policyvault.entities.PolicyVersion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PolicyVersionMapper {
    PolicyVersionResponse toResponse(PolicyVersion version);
}
