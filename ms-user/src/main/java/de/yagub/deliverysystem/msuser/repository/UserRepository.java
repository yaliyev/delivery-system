package de.yagub.deliverysystem.msuser.repository;

import de.yagub.deliverysystem.msuser.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
