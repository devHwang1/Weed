package com.example.weed.config;

import com.example.weed.service.W1001_MemberService;
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
public class W1001_SecurityConfiguration {

    @Autowired
    private W1001_MemberService w1001MemberService; // MemberService를 주입받아 사용자 정보를 가져올 수 있어야 함

    @Autowired
    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final AuthenticationFailureHandler customFailureHandler;

    @Autowired
    public void setW1001MemberService(W1001_MemberService w1001MemberService) {
        this.w1001MemberService = w1001MemberService;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/api/app/member/login").permitAll()
                    .antMatchers("/api/app/member/protected/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                .formLogin() // Form Login 설정
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureHandler(customFailureHandler)
                    .usernameParameter("email")
                    .defaultSuccessUrl("/")
                    .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                    .logoutUrl("/logout")  // 로그아웃 URL 지정
                    .logoutSuccessUrl("/login")  // 로그아웃 성공 시 이동할 페이지 지정
                    .invalidateHttpSession(true)  // 세션 무효화
                    .deleteCookies("JSESSIONID")  // 필요에 따라 쿠키 삭제
                .and()
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
