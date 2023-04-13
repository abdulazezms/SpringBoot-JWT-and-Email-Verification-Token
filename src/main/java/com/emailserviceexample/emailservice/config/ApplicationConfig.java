package com.emailserviceexample.emailservice.config;

import com.emailserviceexample.emailservice.model.AppUser;
import com.emailserviceexample.emailservice.model.Role;
import com.emailserviceexample.emailservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AppUserRepository appUserRepository;
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetails());

        //Adding an admin to check how security works since all users registered are by default ROLE_USER.
        if(!appUserRepository.existsByUsername("admin@gmail.com")) {
            appUserRepository.save(AppUser
                    .builder()
                    .role(Role.ROLE_ADMIN)
                    .password(passwordEncoder().encode("admin"))
                    .username("admin@gmail.com")
                    .isEnabled(true)
                    .build());
        }
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    UserDetailsService userDetails(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<AppUser> appUser = appUserRepository.getByUsername(username);
                if (appUser.isEmpty()) {
                    throw new UsernameNotFoundException("Username doesn't exist");
                }
                return appUser.get();
            }
        };
    }
}
