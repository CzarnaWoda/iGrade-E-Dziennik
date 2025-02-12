package me.igrade.user.repository;

import me.igrade.user.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {


    Authority getAuthorityByAuthorityName(String authority);

    boolean existsByAuthorityName(String authority);

}
