package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("link")
    private String link;

    @JsonProperty("size")
    private Long size;

    @JsonProperty("type")
    private int type;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("users_id")
    @NotNull(message = "User is required")
    private Long usersId;

    @JsonProperty("name")
    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name can be up to 255 characters")
    private String name;

    @JsonProperty("brand_id")
    private Long brandId;
}
