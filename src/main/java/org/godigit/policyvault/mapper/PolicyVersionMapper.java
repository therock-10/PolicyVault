package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.PolicyVersionResponse;
import org.godigit.policyvault.entities.PolicyVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PolicyVersionMapper {
    @Mapping(source = "policy.id", target = "policyId")
    PolicyVersionResponse toResponse(PolicyVersion version);
}
