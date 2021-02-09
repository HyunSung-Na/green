package com.green.demo.repository;

import com.green.demo.model.Id;
import com.green.demo.model.user.ConnectedUser;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);

    List<ConnectedUser> findAllConnectedUser(Id<User, Long> userId);

    List<Id<User, Long>> findConnectedIds(Id<User, Long> userId);

    Optional<User> findById(Id<User, Long> userId);
}
