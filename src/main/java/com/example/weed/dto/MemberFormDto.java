package com.example.weed.dto;

import com.example.weed.domain.members.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Length;

import javax.sql.RowSet;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class MemberFormDto {

    @NotEmpty(message = "️이름은 필수 입력 값입니다.")
//    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    private String dept;

    public Member toEntity() {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        return member;
    }
}
