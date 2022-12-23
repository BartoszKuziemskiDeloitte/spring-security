package com.example.springsecurity.security;

import com.example.springsecurity.exception.ApplicationException;
import com.example.springsecurity.exception.Error;
import com.example.springsecurity.jwt.JwtService;
import com.example.springsecurity.jwt.token.Token;
import com.example.springsecurity.jwt.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApplicationLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String refreshToken = jwtService.getJwtFromCookie(httpServletRequest);
        if (tokenRepository.findByToken(refreshToken).isPresent()) {
            throw new ApplicationException(Error.REFRESH_TOKEN_ALREADY_BLACKLISTED);
        }
        Token token = new Token(refreshToken);
        tokenRepository.save(token);
    }

}
