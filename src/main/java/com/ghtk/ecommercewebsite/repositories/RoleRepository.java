package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import com.ghtk.ecommercewebsite.models.entities.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(RoleEnum name);

}
