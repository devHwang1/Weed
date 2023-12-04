package com.example.weed.authority;

public enum W1001_MemberAuthority {
    USER("USER"), ADMIN("ADMIN");

    private String name;

    private W1001_MemberAuthority(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}