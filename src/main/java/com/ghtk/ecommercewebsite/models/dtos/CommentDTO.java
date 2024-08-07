package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ghtk.ecommercewebsite.models.entities.Comment;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Long id;

    @Size(max = 500, message = "Content must be less than 500 characters")
    @NotNull(message = "Content cannot be null")
    private String content;

    @NotNull(message = "Product item ID cannot be null")
    private Long productItemId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rateStars;

    private Long replyTo;

    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;
}
