package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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
public class CommentDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("content")
    @NotBlank(message = "Content is required")
    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("product_id")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @JsonProperty("user_id")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("rate_stars")
    @Min(value = 1, message = "Rate stars must be at least 1")
    private int rateStars;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;
}
