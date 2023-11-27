package com.example.weed.entity;
import javax.persistence.*;

import com.example.weed.authority.MemberAuthority;
import lombok.*;

import java.nio.file.Path;

@NoArgsConstructor
@Getter
@Data
@Entity(name = "MEMBER")
public class Member {
    // id 컬럼을 MEMBER 테이블의 기본키로 설정
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private MemberAuthority authority;



    @Builder
    public Member(Long id, String password, MemberAuthority authority, String email,String name) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SaveRequest {
        private String id;
        private String name;
        private String password;
        private String email;
        private MemberAuthority authority;

        @Transient
        public Member toEntity() {
            return Member.builder()
                    .id(this.id != null ? Long.valueOf(this.id) : null)
                    .name(this.name)
                    .email(this.email)
                    .password(this.password)
                    .authority(this.authority)
                    .build();
        }
    }
}