package com.emailserviceexample.emailservice.config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       return http
               .csrf().disable()
               .authorizeHttpRequests()
               .requestMatchers("/auth/**").permitAll()
               .requestMatchers("/admins/**").hasAnyAuthority("ROLE_ADMIN")
               .anyRequest()
               .authenticated()
               .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .exceptionHandling(ex -> {
                   ex.accessDeniedHandler(accessDeniedHandler());
                   ex.authenticationEntryPoint(authenticationEntryPoint());

               })
               .build();

    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            //If this exception is thrown, then the user is authenticated but not authorized, therefore, he must be in the security context holder.
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.info("Access Denied exception thrown at " + SecurityContextHolder.getContext().getAuthentication().getName() +
                    " with authorities " + SecurityContextHolder.getContext().getAuthentication().getAuthorities() +
                    " trying to access " + request.getRequestURI());
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        //If this exception is thrown, then the user is not authenticated.
        return (request, response, authException) -> {
             response.setStatus(HttpStatus.FORBIDDEN.value());
             log.info("Current authentication is anonymous!");
         };
    }
}
