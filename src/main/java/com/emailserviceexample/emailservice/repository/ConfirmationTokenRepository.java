package com.emailserviceexample.emailservice.repository;

import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByAppUser(AppUser appUser);
}
