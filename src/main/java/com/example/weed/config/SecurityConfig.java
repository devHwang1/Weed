package com.example.weed.config;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Log4j2
public class SecurityConfig {



    @Configuration
    public class SecurityConfiguration {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers("/members/login", "/members/login-proc").permitAll()
                    .antMatchers("/").authenticated()
                    .anyRequest().permitAll();

            httpSecurity
                    .formLogin() // Form Login 설정
                    .loginPage("/members/login")
                    .loginProcessingUrl("/members/login-proc")
                    .defaultSuccessUrl("/members/main")
                    .and()
                    .logout()
                    .and()
                    .csrf().disable();

            return httpSecurity.build();
        }

    /*비밀번호 암호화하기 위해서 쓰는 PassEncoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}}
