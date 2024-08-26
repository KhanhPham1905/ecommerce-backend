package com.ghtk.ecommercewebsite.models.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCommentRequestDTO {


    @Size(max = 500, message = "Content must be less than 500 characters")
    @NotNull(message = "Content cannot be null")
    private String content;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rateStars;

    private Long replyTo;
}
