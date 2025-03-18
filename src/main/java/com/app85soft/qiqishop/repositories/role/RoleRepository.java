package com.app85soft.qiqishop.repositories.role;

import com.app85soft.qiqishop.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, RoleRepositoryCustom {
}
