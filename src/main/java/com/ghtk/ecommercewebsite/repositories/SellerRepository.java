package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends CrudRepository<Users, Long> {

    @Query(
            "SELECT u FROM Users u " +
                    "JOIN u.roles r " +
                    "WHERE u.email = :email AND r.name = 'SELLER'"
    )
    Optional<Users> findUserWithSellerRoleByEmail(@Param("email") String email);
}
