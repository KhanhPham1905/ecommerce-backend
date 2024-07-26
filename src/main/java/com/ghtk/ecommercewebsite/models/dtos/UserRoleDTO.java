package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRoleDTO {

    @JsonProperty("user_id")
    @Min(value=1, message =  "User ID is required")
    private Long userId;

    @JsonProperty("role_id")
    @Min(value=1, message =  "Role ID is required")
    private Long roleId;

}
