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



    private String description;


    @NotNull(message = "Name is required")
    private String name;


    private boolean status;


    @NotNull(message = "Shop ID is required")
    private Long shopId;
}
