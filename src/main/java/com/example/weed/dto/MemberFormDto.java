package com.example.weed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class MemberFormDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자이상, 16자 이하로 입력해주세요")
    private String password;

    @Builder
    public MemberFormDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
