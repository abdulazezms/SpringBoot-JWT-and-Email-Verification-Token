package com.emailserviceexample.emailservice.config;

import com.emailserviceexample.emailservice.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor//used to inject final fields.
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;





    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.info("doFilterInternal() is called");
        final String authHeader = request.getHeader("Authorization");


        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }


        jwt = authHeader.substring(7);
        final String userEmail;
        /*
        Now that we know that the JWT token exists, we should call the
        user details service to check if we have a user in the storage
        whose email/username matches the token's subject.
         */

        userEmail = jwtService.extractUsername(jwt);
        log.info("User extracted from token " + userEmail);
        //if you get authentication is null in sec context, then it means that the user is not yet authenticated.
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);//retrieve from storage.

            //check if the token is still valid.
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                //Give it more details about this request, such as IP address, certificate serial number, etc.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Update the security context holder with our authentication token.
                //this is basically telling spring that this user is now authenticated.
                //subsequent checks for asking whether this user is authenticated or not will be yes.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Pass on this request to the next filter.
        filterChain.doFilter(request, response);

    }
}
