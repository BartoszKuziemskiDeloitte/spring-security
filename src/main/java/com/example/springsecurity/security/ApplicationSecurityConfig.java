package com.example.springsecurity.security;

import com.example.springsecurity.jwt.JwtConfig;
import com.example.springsecurity.jwt.JwtService;
import com.example.springsecurity.jwt.JwtTokenVerifier;
import com.example.springsecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.springsecurity.user.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.springsecurity.user.role.AuthorityType.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final ApplicationLogoutSuccessHandler applicationLogoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtService))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(applicationLogoutSuccessHandler)
                .deleteCookies(jwtConfig.getRefreshTokenCookieName())
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/users/register", "/users/refresh-token", "/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/users/all").hasAuthority(USER_READ_ALL.getAuthority())
                .antMatchers(HttpMethod.GET, "/users/**").hasAuthority(USER_READ.getAuthority())
                .antMatchers(HttpMethod.PUT, "/users/change-roles").hasAuthority(CHANGE_ROLES.getAuthority())
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(USER_DELETE.getAuthority())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
