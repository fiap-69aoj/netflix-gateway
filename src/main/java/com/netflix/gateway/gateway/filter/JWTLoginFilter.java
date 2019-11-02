package com.netflix.gateway.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.gateway.gateway.dto.AccountCredentials;
import com.netflix.gateway.gateway.service.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 30/10/2019 23:12
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter (String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        AccountCredentials credentials = new ObjectMapper()
                .readValue(request.getInputStream(), AccountCredentials.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(), credentials.getPassword(), Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication (HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication auth) {
        TokenAuthenticationService.addAuthentication(response, auth.getName(), auth.getAuthorities());
    }

}