package com.ghtk.ecommercewebsite.models.entities;


import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "DetailWarehouseMapping",
        classes = @ConstructorResult(
                targetClass = DetailWarehouseDTO.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "country", type = String.class),
                        @ColumnResult(name = "province", type = String.class),
                        @ColumnResult(name = "district", type = String.class),
                        @ColumnResult(name = "commune", type = String.class),
                        @ColumnResult(name = "addressDetail", type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "Warehouse.getDetailWarehouseInfo",
        resultSetMapping = "DetailWarehouseMapping",
        query = "SELECT w.name, a.country, a.province, a.district, a.commune, a.address_detail AS addressDetail " +
                "FROM warehouse w " +
                "JOIN address a ON w.address_id = a.id " +
                "WHERE w.id = :warehouseId " +
                "LIMIT 1"
)

@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
