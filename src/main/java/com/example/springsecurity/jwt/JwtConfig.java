package com.example.springsecurity.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class JwtConfig {

    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.token-prefix}")
    private String tokenPrefix;

    @Value("${application.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${application.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Value("${application.jwt.access-token-cookie-name}")
    private String accessTokenCookieName;

    @Value("${application.jwt.refresh-token-cookie-name}")
    private String refreshTokenCookieName;

}
