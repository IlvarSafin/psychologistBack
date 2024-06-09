package com.example.psyBack.service;

import com.example.psyBack.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final String JWT_SIGNING_KEY = "UHN5S2V5UHN5S2V5UHN5S2V5UHN5S2V5UHN5S2V5UHN5S2V5UHN5S2V5UHN5S2V5";


    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof Users customUserDetails)
        {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("roles", customUserDetails.getRoles());
        }
        return generateToken(claims, userDetails);
    }

    private String generateToken(Map<String, Object> extractClaims, UserDetails userDetails)
    {
        return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, getSigningKey()).compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parser()
                .setSigningKey(JWT_SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SIGNING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
