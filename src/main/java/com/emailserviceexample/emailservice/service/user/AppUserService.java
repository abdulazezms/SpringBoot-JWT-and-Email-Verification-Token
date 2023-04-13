package com.emailserviceexample.emailservice.service.user;

import com.emailserviceexample.emailservice.dto.*;
import com.emailserviceexample.emailservice.model.AppUser;
import org.springframework.http.ResponseEntity;

public interface AppUserService {
    AppUser register(RegistrationRequest registrationRequest);

    AppUserDto getLoggedInUser();


    LoginResponse authenticate(LoginRequest loginRequest);

    Profile getProfile();

    void enableUser(AppUser appUser);
}
