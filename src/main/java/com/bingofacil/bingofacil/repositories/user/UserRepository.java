package com.bingofacil.bingofacil.repositories.user;

import com.bingofacil.bingofacil.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
