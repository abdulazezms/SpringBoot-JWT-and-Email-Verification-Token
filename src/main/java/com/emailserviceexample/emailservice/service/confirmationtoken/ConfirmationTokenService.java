package com.emailserviceexample.emailservice.service.confirmationtoken;

import com.emailserviceexample.emailservice.dto.RegistrationRequest;
import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void verify(String token);
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByAppUser(AppUser appUser);

    void delete(ConfirmationToken confirmationToken);

    ConfirmationToken getNewToken(AppUser appUser);

    void invalidateLastToken(AppUser theUser);

    void sendNewToken(AppUser appUser);
}
