package com.jobster.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtAuthToken {

    @Value("${jwt.secretkey}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private long jwtSecretKeyExpiration;

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    
    public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails){
        return buildToken(extraClaims,userDetails,jwtSecretKeyExpiration);
    }

    private String buildToken(Map<String,Object> extraClaims,UserDetails userDetails,long expiration){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }    

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignKey(){
        byte[] keys = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keys);
    }
    

}
