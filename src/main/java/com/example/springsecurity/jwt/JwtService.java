package com.example.springsecurity.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springsecurity.exception.ApplicationException;
import com.example.springsecurity.exception.Error;
import com.example.springsecurity.redis.RedisRepository;
import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserRepository;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;

    public Map<String, String> refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            throw new JWTVerificationException("Refresh token empty or with no prefix");
        }
        String refreshToken = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        if (this.findToken(refreshToken) != null) {
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

            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("authorities", authorities)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                    .withIssuedAt(new Date())
                    .sign(algorithm);

            return Collections.singletonMap("access_token", accessToken);

        } catch (JWTCreationException exception) {
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(exception.getMessage(), exception.getCause());
        }
    }

    public String blacklistJwt(String token) {
        return redisRepository.save(token);
    }

    public String findToken(String token) {
        return redisRepository.findToken(token);
    }
}
