package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepository extends CrudRepository<User, Long> {

    @Query(
            "SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.email = :email AND r.name = 'SELLER'"
    )
    Optional<User> findUserWithSellerRoleByEmail(@Param("email") String email);

}
