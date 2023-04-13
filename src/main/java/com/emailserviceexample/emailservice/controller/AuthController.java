package com.emailserviceexample.emailservice.controller;

import com.emailserviceexample.emailservice.dto.AppUserDto;
import com.emailserviceexample.emailservice.dto.LoginRequest;
import com.emailserviceexample.emailservice.dto.LoginResponse;
import com.emailserviceexample.emailservice.dto.RegistrationRequest;
import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.service.confirmationtoken.ConfirmationTokenService;
import com.emailserviceexample.emailservice.service.user.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest){
        log.info("Received registration for {}", registrationRequest.getEmail());
        AppUser appUser = appUserService.register(registrationRequest);
        confirmationTokenService.sendNewToken(appUser);
        return new ResponseEntity<>(
                String.format("Please verify your email by clicking on the link sent to: %s", registrationRequest.getEmail())
                , HttpStatus.CREATED);
    }
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = appUserService.authenticate(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String confirmationToken){
        confirmationTokenService.verify(confirmationToken);
        return new ResponseEntity<>("Email has been verified", HttpStatus.OK);
    }
}
