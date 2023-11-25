package com.example.weed.domain.members;

import com.example.weed.domain.dept.Dept;
import com.example.weed.dto.MemberFormDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "deptName")
    private String deptName;

    // 회원이 속한 부서
    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String name, String email, String password, MemberRole role,Dept dept) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dept = dept;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))  //암호화처리
                .role(MemberRole.USER)
                .build();
        return member;
    }

}
