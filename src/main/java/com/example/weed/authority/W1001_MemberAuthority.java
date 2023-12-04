package com.example.weed.authority;


import lombok.Getter;

@Getter
public enum W1001_MemberAuthority {
    USER("USER"), ADMIN("ADMIN"), SCANNER("SCANNER");

    private String name;

    private W1001_MemberAuthority(String name) {
        this.name = name;
    }

}