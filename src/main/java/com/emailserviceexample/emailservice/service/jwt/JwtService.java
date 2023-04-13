package com.emailserviceexample.emailservice.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    //THIS IS NOT RECOMMENDED FOR USAGE IN PRODUCTION (USE SOMETHING LIKE AWS KMS OR Azure Key Vault, etc.)
    private final String secretKey = "7638792F423F4528482B4D6250655368566D597133743677397A24432646294A";
    private final int tokenValidityInMinutes = 30;
    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimResolver){
        /*
        This method is used to extract a single claim.
         */
        final Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        /*
        To extract all claims, we need to return jwts.parseBuilder() and set the signing key.
        Because when we try to generate/decode a token, we need to use the signing key.
         */
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();//we can get all the claims within the token using getBody
    }

    private Key getSigningKey() {
        /*
        create a byte array that holds the key decoded.
         */
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateToken(UserDetails userDetails) {
        /*
        This allows us to generate a token without adding extra claims.
         */
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        /*
        The map contains the claims (extra ones) that we want to add. The extra claims
        can be used if we want to pass authorities, any info that we want to store in the token.
         */

        return Jwts.
                builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())//The username (unique) can be email, for example.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * tokenValidityInMinutes))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        /*
        To validate the token, we need the user details, because we want
        to validate that the token belongs to this user.
         */
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        //if the expiration date is before the current date, then it is expired.
        return extractClaim(jwt, (Claims::getExpiration)).before(new Date());
    }
}
