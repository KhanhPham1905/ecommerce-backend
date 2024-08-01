package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class CartDTO {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private Long userId;


    @JsonIgnore
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
