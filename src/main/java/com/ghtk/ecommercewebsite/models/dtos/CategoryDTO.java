package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class CategoryDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("shop_id")
    @NotNull(message = "Shop ID is required")
    private Long shopId;
}
