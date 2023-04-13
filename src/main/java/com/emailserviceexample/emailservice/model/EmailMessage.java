package com.emailserviceexample.emailservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String body;
}
