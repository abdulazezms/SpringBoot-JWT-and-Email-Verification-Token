package com.emailserviceexample.emailservice.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "confirmation_tokens")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    @Column(nullable = false, name = "expires_at")
    private Date expiresAt;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;





}
