package io.app.adfly.repositories;

import io.app.adfly.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByAuthority(String authority);

}
