package com.example.weed.authority;

import lombok.Getter;

@Getter
public enum MemberAuthority {
    USER("USER"), ADMIN("ADMIN");

    private final String name;

    private MemberAuthority(String name) {
        this.name = name;
    }

}