package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Setter
@Getter
public class DetailInventoryDTO {
    @NotNull(message = "Quantity is required")
    @JsonProperty("quantity")
    private Integer quantity;

    @NotBlank(message = "Sku code is required")
    @JsonProperty("sku_code")
    private String skuCode;

    @NotBlank(message = "Name Product is required")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Warehouse is required")
    private String warehouse;

    @NotBlank(message = "Supplier is required")
    private String supplier;

    @NotNull(message = "Unit price is required")
    @JsonProperty("unit_price")
    private  Long unitPrice;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("warehouse_id")
    @NotNull(message = "Warehouse id is required")
    private Long warehouseId;

    @NotBlank(message = "location is required")
    private String location;

    public DetailInventoryDTO(Integer quantity, String skuCode, String name, String warehouse) {
        this.quantity = quantity;
        this.skuCode = skuCode;
        this.name = name;
        this.warehouse = warehouse;
    }

    public DetailInventoryDTO(Integer quantity, String skuCode, String name,String supplier, Long unitPrice, String warehouse, String location) {
        this.quantity = quantity;
        this.skuCode = skuCode;
        this.name = name;
        this.supplier = supplier;
        this.unitPrice = unitPrice;
        this.warehouse = warehouse;
        this.location = location;
    }

}
