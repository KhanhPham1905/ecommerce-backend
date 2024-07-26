package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class RoleDTO {
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Role name is required")
    @JsonProperty("name")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}
