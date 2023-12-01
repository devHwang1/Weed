package com.example.weed.entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.weed.authority.MemberAuthority;
import lombok.*;

import java.nio.file.Path;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Data
@Entity
public class Member {
    // id 컬럼을 MEMBER 테이블의 기본키로 설정
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private MemberAuthority authority;
    private Date registrationTime;



    @Builder
    public Member(Long id, String password, MemberAuthority authority, String email,String name,Date registrationTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.authority = authority;
        this.registrationTime = registrationTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SaveRequest {
        private String id;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        private MemberAuthority authority;

        private Date registrationTime;

        @Transient
        public Member toEntity() {
            return Member.builder()
                    .id(this.id != null ? Long.valueOf(this.id) : null)
                    .name(this.name)
                    .email(this.email)
                    .password(this.password)
                    .authority(this.authority)
                    .registrationTime(this.registrationTime)
                    .build();
        }
    }

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;
}