package com.example.springsecurity.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix()) || request.getServletPath().equals("/users/refresh-token")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

//        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(jwtConfig.getAccessTokenCookieName()))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new JWTVerificationException("None authorization cookie found"));

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(exception.getMessage(), exception.getCause());
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        }

        filterChain.doFilter(request, response);
    }
}
