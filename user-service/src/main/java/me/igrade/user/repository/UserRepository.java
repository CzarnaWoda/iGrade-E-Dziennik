package me.igrade.user.repository;

import me.igrade.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findUserByEmail(String email);

    List<User> findUsersByFirstName(String firstName);

    List<User> findUsersByLastName(String lastName);

    Optional<User> findUserById(long userId);

    List<User> findUsersByClassId(long studentId);


    boolean existsUserByEmail(String email);
}
