package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.PolicyCreateRequest;
import org.godigit.policyvault.dto.PolicyUpdateRequest;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.dto.PolicyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PolicyMapper {
    Policy toEntity(PolicyCreateRequest request);
    Policy toEntity(PolicyUpdateRequest request);
    PolicyResponse toDto(Policy policy);

}

