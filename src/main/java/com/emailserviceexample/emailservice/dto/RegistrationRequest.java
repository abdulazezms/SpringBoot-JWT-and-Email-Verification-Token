package com.emailserviceexample.emailservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @Email(message = "email must be valid")
    private String email;
    @Size(min = 6, message = "password's length must be at least 6 characters")
    private String password;


}
