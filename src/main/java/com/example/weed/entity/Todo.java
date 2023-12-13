package com.example.weed.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private boolean checked;

    private Date registrationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Todo(String title, String content, boolean checked, Date registrationTime, Member member) {
        this.title = title;
        this.content = content;
        this.checked = checked;
        this.registrationTime = registrationTime;
        this.member = member;
    }
}
