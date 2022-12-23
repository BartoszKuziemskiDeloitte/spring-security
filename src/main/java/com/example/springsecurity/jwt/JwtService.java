package com.example.springsecurity.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springsecurity.exception.ApplicationException;
import com.example.springsecurity.exception.Error;
import com.example.springsecurity.jwt.token.TokenRepository;
import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public Map<String, String> refreshToken(HttpServletRequest request) {
        String refreshToken = this.getJwtFromCookie(request);

        if (tokenRepository.findByToken(refreshToken).isPresent()) {
            throw new ApplicationException(Error.REFRESH_TOKEN_BLACKLISTED);
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            if (!decodedJWT.getClaim("authorities").isMissing()) {
                throw new JWTVerificationException("Token is not a refresh token");
            }
            String username = decodedJWT.getSubject();
            User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            List<String> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                Set<GrantedAuthority> authoritiesFromRole = role.getRoleType().getGrantedAuthorities();
                authorities.addAll(authoritiesFromRole
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
            });

            String accessToken = this.createAccessToken(user.getUsername(), authorities, algorithm);
            return Collections.singletonMap("access_token", accessToken);

        } catch (JWTCreationException exception) {
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(exception.getMessage(), exception.getCause());
        }
    }

    public String createAccessToken(String username, List<String> authorities, Algorithm algorithm) {
        return JWT.create()
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration() * 1000))
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public String createRefreshToken(String username, Algorithm algorithm) {
       return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration() * 1000))
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(jwtConfig.getRefreshTokenCookieName()))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new JWTVerificationException("None authorization cookie found"));
    }

    public ResponseCookie createCookie(String token) {
        return ResponseCookie.from(jwtConfig.getRefreshTokenCookieName(), token)
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .httpOnly(true)
                .path("/")
                .secure(false) // change to true! false is only for testing
                .sameSite("Lax")
                .build();
    }

}
