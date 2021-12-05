package io.app.adfly.services;

import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserView;
import io.app.adfly.entities.Role;
import io.app.adfly.entities.User;
import io.app.adfly.repositories.RoleRepository;
import io.app.adfly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Transactional
    public UserView create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var role = roleRepository.findByAuthority(request.getAuthority());
        if(!role.isPresent()){
            var newRole = new Role(request.getAuthority());
            role = Optional.of(roleRepository.save(newRole));
        }
        user.setRole(role.get());
        user = userRepository.save(user);

        UserView uv = new UserView();
        uv.setUsername(user.getUsername());
        return uv;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(format("User with username - %s, not found", username))
                );
    }
}
