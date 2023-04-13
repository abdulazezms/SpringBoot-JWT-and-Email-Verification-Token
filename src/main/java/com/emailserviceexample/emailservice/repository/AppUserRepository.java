package com.emailserviceexample.emailservice.repository;

import com.emailserviceexample.emailservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> getByUsername(String username);
    boolean existsByUsername(String username);
}
