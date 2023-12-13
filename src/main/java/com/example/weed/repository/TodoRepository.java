package com.example.weed.repository;

import com.example.weed.entity.Member;
import com.example.weed.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    Long countByMember(Member member);

    Long countByMemberAndChecked(Member member, boolean b);
}
