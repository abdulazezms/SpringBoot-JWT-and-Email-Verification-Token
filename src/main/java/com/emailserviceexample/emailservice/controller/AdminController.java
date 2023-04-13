package com.emailserviceexample.emailservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @GetMapping
    public String helloAdmin(){
        return "Hello Admin!";
    }
}
