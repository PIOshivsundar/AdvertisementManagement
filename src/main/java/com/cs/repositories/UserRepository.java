package com.cs.repositories;

import com.cs.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByAadhaar(String aadhaar);
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.email =:email")
    User getUserByUserName(@Param("email") String email);
    User findByResetPasswordToken(String token);

    @Query(value = "SELECT u FROM User u")
    List<User> findAllUsers();
}
