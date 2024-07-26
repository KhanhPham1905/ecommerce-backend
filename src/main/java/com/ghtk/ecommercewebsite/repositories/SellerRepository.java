package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepository extends CrudRepository<Users, Long> {

    @Query(
            "SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.email = :email AND r.name = 'SELLER'"
    )
    Optional<Users> findUserWithSellerRoleByEmail(@Param("email") String email);

}
