package com.app85soft.qiqishop.repositories.user;

import com.app85soft.qiqishop.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    boolean existsByPhone(String phone);
}
