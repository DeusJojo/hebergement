package com.afpa.hebergement.security.jwt;

import com.afpa.hebergement.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getBeneficiaryNumber())
                .setIssuedAt(new Date())
                .claim("authorities", userDetails.getAuthorities())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getBenefNumberFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key()).build();
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }catch (SecurityException e){
            logger.error("JWT signature is invalid {}", e.getMessage());
        }

        return false;
    }
    }

