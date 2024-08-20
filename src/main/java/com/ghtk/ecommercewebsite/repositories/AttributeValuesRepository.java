package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.AttributeValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValuesRepository extends JpaRepository<AttributeValues, Long> {

    @Modifying
    @Query("UPDATE AttributeValues av SET av.isDelete = true WHERE av.id = :id")
    void softDeleteById(Long id);

    @Query("SELECT av FROM AttributeValues av WHERE av.attributeId = :id AND av.isDelete = false")
    List<AttributeValues> findAttributeValuesByAttributeId(Long id);


//    @Query("SELECT av FROM AttributeValues av WHERE av.attri")
//    List<AttributeValues> findAllByAttributeId(Long id);
}
