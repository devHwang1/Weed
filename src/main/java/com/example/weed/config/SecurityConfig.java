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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((request) -> {
//                    request.requestMatchers("/","/members/new").permitAll()
//                            .anyRequest().authenticated();
//                })
//                .formLogin(Customizer.withDefaults());
//
//         return http.build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {



        @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll()
                        .requestMatchers( "/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
            return (web) -> web.ignoring().requestMatchers(String.valueOf(PathRequest.
                    toStaticResources().atCommonLocations()));
    }



    /*비밀번호 암호화하기 위해서 쓰는 PassEncoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
