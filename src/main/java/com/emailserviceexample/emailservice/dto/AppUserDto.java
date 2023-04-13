package com.emailserviceexample.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AppUserDto {

    private String username;

    private boolean isEnabled;
}
