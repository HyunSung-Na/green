package com.green.demo.repository.user;

import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);
}
