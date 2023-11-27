package com.example.weed.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private final AuthenticationFailureHandler authenticationFailureHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests() // 요청에 대한 권한 설정
                .antMatchers("/").permitAll()
                .anyRequest().permitAll();

        httpSecurity
                .formLogin() // Form Login 설정
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .and()
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
