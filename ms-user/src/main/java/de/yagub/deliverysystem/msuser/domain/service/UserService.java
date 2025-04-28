package de.yagub.deliverysystem.msuser.domain.service;

import de.yagub.deliverysystem.msuser.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    User register(String username, String plainPassword);
    User login(String username, String plainPassword);
    boolean usernameExists(String username);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
