package com.example.weed.entity;

import com.example.weed.authority.W1001_MemberAuthority;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @Enumerated(EnumType.STRING)
    private W1001_MemberAuthority authority;

    private Date registrationTime;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Working> workings;
//    @ManyToOne
//    @JoinColumn(name = "file_id")  // 외래 키 명칭 수정
//    private File file;

    @Builder
    public Member(Long id, String name, String email, String password, W1001_MemberAuthority authority, Date registrationTime,File file, Dept dept) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.registrationTime = registrationTime;
        this.file = file;
        this.dept = dept;
//        this.file = file;
    }

    @Getter
    @Setter
    public static class SaveRequest {
        private String id;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        private W1001_MemberAuthority authority;

        private Date registrationTime;

        private Long dept_id = 0L;

        public SaveRequest() {
            this.dept_id = Long.valueOf("0");
        }
        @Transient
        public Member toEntity() {
            return Member.builder()
                    .id(this.id != null ? Long.valueOf(this.id) : null)
                    .name(this.name)
                    .email(this.email)
                    .password(this.password)
                    .authority(this.authority)
                    .registrationTime(this.registrationTime)
                    .dept(new Dept(dept_id))
                    .build();
        }
    }
}