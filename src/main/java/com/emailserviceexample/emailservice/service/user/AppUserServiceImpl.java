package com.emailserviceexample.emailservice.service.user;

import com.emailserviceexample.emailservice.dto.*;
import com.emailserviceexample.emailservice.exception.EmailNotVerifiedException;
import com.emailserviceexample.emailservice.exception.RegistrationFailedException;
import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.model.ConfirmationToken;
import com.emailserviceexample.emailservice.model.Role;
import com.emailserviceexample.emailservice.repository.AppUserRepository;
import com.emailserviceexample.emailservice.repository.ConfirmationTokenRepository;
import com.emailserviceexample.emailservice.service.confirmationtoken.ConfirmationTokenService;
import com.emailserviceexample.emailservice.service.jwt.JwtService;
import com.emailserviceexample.emailservice.utils.EmailMessagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AppUser register(RegistrationRequest registrationRequest) {
        log.info("Registering {} is in process", registrationRequest.getEmail());

        String email = registrationRequest.getEmail().toLowerCase();
        Optional<AppUser> appUser = null;
        if ((appUser = appUserRepository.getByUsername(email)).isPresent()
                && appUser.get().isEnabled()) {
            //user is enabled/verified.
            log.info("email already exists");
            throw new RegistrationFailedException("Email already exists.");
        } else if (appUser.isPresent()) {
            log.info("user is already registered but not enabled yet.");
            return appUser.get();
        } else {
            //new registered user.
            AppUser newUser = registerationRequestToAppUser(registrationRequest);
            appUserRepository.save(newUser);
            log.info("registration completed");
            return newUser;
        }

    }

    @Override
    public AppUserDto getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser appUser = appUserRepository.getByUsername(email).get();
        return appUserToAppUserDto(appUser);
    }


    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating...");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        log.info("Passed authentication successfully...");


        AppUser appUser = appUserRepository
                .getByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username doesn't exist!"));
        if (!appUser.isEnabled()) {
            throw new EmailNotVerifiedException("Sorry, your email hasn't been verified yet. Check out your email" +
                    " and verify your account by clicking on the link sent.");
        }
        String jwt = jwtService.generateToken(appUser);
        return LoginResponse
                .builder()
                .token(jwt)
                .build();
    }

    @Override
    public Profile getProfile() {
        return appUserToProfile();
    }

    private Profile appUserToProfile() {
        return Profile
                .builder()
                .email(getLoggedInUser().getUsername())
                .build();
    }

    @Override
    public void enableUser(AppUser appUser) {
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }

    private AppUserDto appUserToAppUserDto(AppUser appUser) {
        return AppUserDto
                .builder()
                .username(appUser.getUsername())
                .isEnabled(appUser.isEnabled())
                .build();
    }

    private AppUser registerationRequestToAppUser(RegistrationRequest registrationRequest) {
        return AppUser
                .builder()
                .role(Role.ROLE_USER)
                .username(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();
    }
}
