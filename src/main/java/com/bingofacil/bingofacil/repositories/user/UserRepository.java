package com.bingofacil.bingofacil.repositories.user;

import com.bingofacil.bingofacil.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);
}
