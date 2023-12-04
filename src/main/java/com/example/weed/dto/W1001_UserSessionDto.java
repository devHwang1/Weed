package com.example.weed.dto;

import com.example.weed.authority.W1001_MemberAuthority;
import com.example.weed.entity.Member;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@Getter
public class W1001_UserSessionDto implements Serializable {
    private String name;
    private String email;
    private String password;
    private String deptName;
    private String fileName;
    private W1001_MemberAuthority authority;

    public W1001_UserSessionDto(Member member){
        this.name = member.getName();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.deptName = member.getDept().getDeptName();

        // null 체크를 추가하여 NPE 방지
        if (member.getFile() != null) {
            this.fileName = member.getFile().getFileName();
        }

        this.authority = member.getAuthority();
    }
}
