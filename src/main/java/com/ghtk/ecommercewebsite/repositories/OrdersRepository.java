package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
