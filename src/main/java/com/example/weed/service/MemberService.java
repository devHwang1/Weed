package com.example.weed.service;

import com.example.weed.dto.UserSessionDto;
import com.example.weed.entity.Member;
import com.example.weed.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
public class MemberService implements UserDetailsService {



    private final HttpSession session;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(HttpSession session, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.session = session;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Member.SaveRequest member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email"));

        session.setAttribute("username", new UserSessionDto(member));

        return toUserDetails(member);
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(new SimpleGrantedAuthority(member.getAuthority().getName()))
                .build();
    }

}
