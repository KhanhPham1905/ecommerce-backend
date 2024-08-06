package com.ghtk.ecommercewebsite.models.entities;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "DetailSupplyMapping",
        classes = @ConstructorResult(
                targetClass = DetailInventoryDTO.class,
                columns = {
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "sku_code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "supplier", type = String.class),
                        @ColumnResult(name = "unit_price", type = Long.class),
                        @ColumnResult(name = "warehouse", type = String.class),
                        @ColumnResult(name = "location", type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "Supply.getAllImport",
        query = "SELECT s.quantity, pi.sku_code, p.name, s.supplier, s.unit_price, w.name AS warehouse, s.location " +
                "FROM Supply s " +
                "INNER JOIN Product_item pi ON pi.id = s.product_item_Id " +
                "INNER JOIN Warehouse w ON w.id = s.warehouse_Id " +
                "INNER JOIN Product p ON p.id = pi.product_Id " +
                "WHERE  s.status = 1 AND w.shop_Id = :shop_id ",
        resultSetMapping = "DetailSupplyMapping"
)

@NamedNativeQuery(
        name = "Supply.getAllExport",
        query = "SELECT s.quantity, pi.sku_code, p.name, s.supplier, s.unit_price, w.name AS warehouse, s.location " +
                "FROM Supply s " +
                "INNER JOIN Product_item pi ON pi.id = s.product_item_Id " +
                "INNER JOIN Warehouse w ON w.id = s.warehouse_Id " +
                "INNER JOIN Product p ON p.id = pi.product_Id " +
                "WHERE  s.status = 0 AND w.shop_Id = :shop_id",
        resultSetMapping = "DetailSupplyMapping"
)






@Entity
@Table(name = "supply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supply_date")
    private LocalDateTime supplyDate;

    private int quantity;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_item_id", nullable = false)
    private Long productItemId;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "unit_price")
    private  Long unitPrice;

    @Column(name = "location")
    private  String location;

    @Column(name = "status")
    private  Boolean status;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
