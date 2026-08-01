package com.dmitry.devboard.security;

import com.dmitry.devboard.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user){
        Date nowTime = new Date();
        Date expirationTime = new Date(nowTime.getTime() + expiration);
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(nowTime)
                .expiration(expirationTime)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey(){
        byte[] keyArr = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyArr);
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(UserDetails userDetails, String token){
        String emailInToken = extractUsername(token);
        return emailInToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    private boolean isTokenExpired(String token){
        Date tokenDate = extractExpiration(token);
        return tokenDate.before(new Date());
    }

}
