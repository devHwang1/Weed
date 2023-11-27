package com.example.weed.dto;

import com.example.weed.domain.dept.Dept;
import com.example.weed.domain.members.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class MemberFormDto extends User implements UserDetails {

    @NotEmpty(message = "️이름은 필수 입력 값입니다.")
//    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    private String dept;



    public MemberFormDto(String email, String password, Collection<? extends GrantedAuthority> authorities, String name, Dept dept) {
        super(email, password, authorities != null ? authorities : Collections.emptyList());
        this.name = name;
        this.email = email;
        this.password = password;
        this.dept = String.valueOf(dept);
    }

    public Member toEntity() {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        return member;
    }


//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        // 실제 권한 정보를 반환하도록 수정 (여기서는 빈 권한 리스트 반환)
//        return Collections.emptyList();
//    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        // 권한이 필요하다면 적절한 권한을 반환하도록 수정
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return getEmail();
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
