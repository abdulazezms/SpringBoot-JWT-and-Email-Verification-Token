package com.emailserviceexample.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String message;
    private Date timestamp;
    private Integer statusCode;
}
