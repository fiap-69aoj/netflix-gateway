package com.netflix.gateway.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 30/10/2019 23:11
 */
public class TokenAuthenticationService {

    static final long EXPIRATION_TIME = Duration.ofDays(10).toMillis();
    static final String SECRET = "FIAP_69AOJ_MY_SECRETS";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication (HttpServletResponse response, String username,
            final Collection<? extends GrantedAuthority> authorities) {

        final String roles = authorities.stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        final String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            final Claims jwtClaims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            if (jwtClaims.getSubject() != null) {
                final String roles = jwtClaims.get("roles", String.class);
                final List<SimpleGrantedAuthority> autoritiesRoles = Stream.of(roles.split(","))
                        .map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
                return new UsernamePasswordAuthenticationToken(jwtClaims.getSubject(), null, autoritiesRoles);
            }
        }
        return null;
    }

}
