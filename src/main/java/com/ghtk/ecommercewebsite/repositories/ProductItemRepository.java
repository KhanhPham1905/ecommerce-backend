package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.DetailProductItemDTO;
import com.ghtk.ecommercewebsite.models.dtos.ListAttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    ProductItem findBySkuCode(String skuCode);

    @Query(name = "ProductItem.GetAllProductItemByProductId", nativeQuery = true)
    List<DetailProductItemDTO> findAllProductItemByProductId(@Param("product_id") Long productId);

    @Query(value = "SELECT * FROM Product_item pi " +
            "WHERE pi.product_id = ?1 "+
            "AND pi.is_delete = 0 "+
            "ORDER BY pi.id "
            , nativeQuery = true)
    List<ProductItem> findAllByProductId(Long productId);


    void deleteByProductId(Long productId);
    @Query(value = "SELECT p.shop_id FROM product_item pi JOIN product p ON pi.product_id = p.id WHERE pi.id = :productItemId", nativeQuery = true)
    Long findShopIdByProductItemId(@Param("productItemId") Long productItemId);

    @Modifying
    @Query("UPDATE ProductItem pi SET pi.isDelete = true WHERE pi.productId = :id")
    void softDeleteProductItemByProductId(Long id);

//    List<ProductItem> findAllByProductId(Long id);

//    @Query("")
//    List<ProductItem> findProductItemByAttributesValues(Long id, ListAttributeValuesDTO listAttributeValuesDTO);

}
