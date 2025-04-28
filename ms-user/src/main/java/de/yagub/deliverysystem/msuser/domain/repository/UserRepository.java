package de.yagub.deliverysystem.msuser.domain.repository;

import de.yagub.deliverysystem.msuser.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);


}
