package com.ishop.sbinternetshop.repositories;

import com.ishop.sbinternetshop.model.AppRole;
import com.ishop.sbinternetshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole roleName);
}
