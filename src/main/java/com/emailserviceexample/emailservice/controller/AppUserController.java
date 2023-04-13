package com.emailserviceexample.emailservice.controller;


import com.emailserviceexample.emailservice.dto.Profile;
import com.emailserviceexample.emailservice.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;
    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile(){
        return new ResponseEntity<>(appUserService.getProfile(), HttpStatus.OK);
    }
}
