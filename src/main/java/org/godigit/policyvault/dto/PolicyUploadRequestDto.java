package org.godigit.policyvault.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyUploadRequestDto {
    private String policyName;
    private String description;
    private String policyText;
}