package me.igrade.user.repository;

import me.igrade.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {


    UserRole getUserRoleByName(String name);

    boolean existsByName(String name);


}
