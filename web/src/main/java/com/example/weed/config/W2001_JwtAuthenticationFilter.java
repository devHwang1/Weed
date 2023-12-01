//package com.example.weed.config;
//
//import com.example.weed.service.W2001_QrJwtService;
//import io.jsonwebtoken.ExpiredJwtException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//
//public class W2001_JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    private final W2001_QrJwtService jwtService;
//
//    public W2001_JwtAuthenticationFilter(
//            AuthenticationManager authenticationManager,
//            W2001_QrJwtService jwtService) {
//        super("/api/app/member/login");
//        setAuthenticationManager(authenticationManager);
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//        try {
//            String jwtToken = extractJwtToken(request);
//            if (jwtToken != null && jwtService.validateToken(jwtToken)) {
//                Authentication authentication = new UsernamePasswordAuthenticationToken(
//                        jwtService.getUserIdFromToken(jwtToken),
//                        jwtService.getEmailFromToken(jwtToken),
//                        jwtService.getAuthoritiesFromToken(jwtToken));
//
//                return getAuthenticationManager().authenticate(authentication);
//            }
//        } catch (ExpiredJwtException e) {
//            // 만료된 토큰 예외 처리
//            // 여기서도 예외를 던지거나 다른 처리를 수행할 수 있습니다.
//        }
//
//        // 인증 실패 시 null 반환
//        return null;
//    }
//
//    private String extractJwtToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}