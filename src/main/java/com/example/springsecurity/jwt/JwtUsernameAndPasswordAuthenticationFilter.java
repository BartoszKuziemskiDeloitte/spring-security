package com.example.springsecurity.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecurity.exception.ApplicationException;
import com.example.springsecurity.exception.Error;
import com.example.springsecurity.user.dto.UsernameAndPasswordDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@AllArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordDto authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordDto.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            return authenticationManager.authenticate(authentication);

        } catch (IOException exception) {
            throw new ApplicationException(Error.AUTHENTICATION_EXCEPTION);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
        String accessToken = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("authorities", authResult
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration() * 1000))
                .withIssuedAt(new Date())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(authResult.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration() * 1000))
                .withIssuedAt(new Date())
                .sign(algorithm);

        ResponseCookie cookie = ResponseCookie.from(jwtConfig.getRefreshTokenCookieName(), refreshToken)
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .httpOnly(true)
                .path("/")
                .secure(false)
                .sameSite("Lax")
                .build();

        response.setHeader(SET_COOKIE, cookie.toString());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), Collections.singletonMap("access_token", accessToken));

    }
}
