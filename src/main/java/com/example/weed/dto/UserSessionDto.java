package com.example.weed.dto;

import com.example.weed.authority.MemberAuthority;
import com.example.weed.entity.Member;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;

@Data
@Getter
public class UserSessionDto implements Serializable {
    private String name;
    private String email;
    private String password;
    private MemberAuthority authority;

    public UserSessionDto(Member member){
        this.name = member.getName();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.authority =member.getAuthority();
    }
}
