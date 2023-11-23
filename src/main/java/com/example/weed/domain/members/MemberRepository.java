package com.example.weed.domain.members;

import com.example.weed.dto.MemberFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    Member findByEmail(String email);

    ArrayList<Member> findAll();
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
