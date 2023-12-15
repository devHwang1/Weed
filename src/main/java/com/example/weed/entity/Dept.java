package com.example.weed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Dept {

    @Id
    @GeneratedValue
    private Long id;

    private String deptName;

    @OneToMany(mappedBy = "dept")
    private List<Member> members;

    public Dept() {
        // Default constructor
    }

    // Constructor with id parameter
    public Dept(Long id) {
        this.id = id;
    }

    // Constructor with deptName parameter
    public Dept(String deptName) {
        this.deptName = deptName;
    }
}
