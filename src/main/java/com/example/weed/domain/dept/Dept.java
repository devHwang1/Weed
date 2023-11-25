// Dept 엔티티

package com.example.weed.domain.dept;

import com.example.weed.domain.members.Member;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Getter
@NoArgsConstructor
@Table(name = "dept")
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dept_id")
    private Long id;

    private String deptName;

    // 부서에서 회원을 참조
    @OneToMany(mappedBy = "dept")
    private List<Member> members;

}
