package com.emailserviceexample.emailservice.service.confirmationtoken;

import com.emailserviceexample.emailservice.exception.InvalidVerificationTokenException;
import com.emailserviceexample.emailservice.exception.TokenExpiredException;
import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.model.ConfirmationToken;
import com.emailserviceexample.emailservice.repository.ConfirmationTokenRepository;
import com.emailserviceexample.emailservice.service.user.AppUserService;
import com.emailserviceexample.emailservice.utils.EmailMessagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final AppUserService appUserService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailMessagingUtils emailMessagingUtils;
    @Value("${verification.expiration.in.minutes}")
    private Integer minutes;

    @Override
    public void verify(String token) {
        log.info("verifying token...");
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByToken(token);
        if(optionalConfirmationToken.isEmpty()){
            throw new InvalidVerificationTokenException("Token isn't valid.");
        }
        ConfirmationToken confirmationToken = optionalConfirmationToken.get();
        Date date = confirmationToken.getExpiresAt();
        if (date.before(new Date())) {
            throw new TokenExpiredException("The token has expired.");
        }
        log.info("token is valid");
        appUserService.enableUser(confirmationToken.getAppUser());
        delete(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public Optional<ConfirmationToken> findByAppUser(AppUser appUser) {
        return confirmationTokenRepository.findByAppUser(appUser);
    }

    @Override
    public void delete(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    @Override
    public ConfirmationToken getNewToken(AppUser appUser) {
        Date expiryDate = new Date(System.currentTimeMillis() + (1000 * 60) * minutes);
        return ConfirmationToken
                    .builder()
                    .token(UUID.randomUUID().toString())
                    .expiresAt(expiryDate)
                    .appUser(appUser)
                    .build();
    }

    @Override
    public void invalidateLastToken(AppUser theUser) {
        log.info("invalidating last token of {}, if exists...", theUser.getUsername());
        Optional<ConfirmationToken> prevConfirmationToken = getAssociatedToken(theUser);
        prevConfirmationToken.ifPresent(confirmationTokenRepository::delete);
        log.info("token has been invalidated");
    }

    @Override
    public void sendNewToken(AppUser appUser) {
        invalidateLastToken(appUser);
        log.info("sending new token to {}", appUser.getUsername());
        ConfirmationToken newToken = getNewToken(appUser);
        confirmationTokenRepository.save(newToken);
        emailMessagingUtils.sendVerificationEmail(appUser.getUsername(), newToken.getToken());
        log.info("token has been sent");
    }

    private Optional<ConfirmationToken> getAssociatedToken(AppUser appUser){
        return confirmationTokenRepository.findByAppUser(appUser);
    }
}
