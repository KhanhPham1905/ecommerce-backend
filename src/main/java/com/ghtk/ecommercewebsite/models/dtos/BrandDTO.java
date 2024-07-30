package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    @JsonProperty("id")
    private Long id;

     @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime modifiedAt;

    @JsonProperty("description")
    private String description;

    @JsonProperty("name")
    @NotNull(message = "Name is required")
    private String name;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("shop_id")
    @NotNull(message = "Shop ID is required")
    private Long shopId;
}
