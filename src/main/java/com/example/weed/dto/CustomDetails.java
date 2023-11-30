package com.example.weed.dto;

import com.example.weed.entity.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Setter
@Getter
public class CustomDetails implements UserDetails {
    private final String username;
    private final String name;
    private final String password;
    private final String authorities;
    private final String deptName;
    private final Member loggedInMember;

    public CustomDetails(String username, String name, String password, String authorities,String deptName,Member loggedInMember) {
        this.username = username;
        this.deptName =deptName;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
        this.loggedInMember = loggedInMember;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authorities));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
