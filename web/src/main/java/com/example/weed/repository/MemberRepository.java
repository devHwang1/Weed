package com.example.weed.repository;

import com.example.weed.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.password = :password WHERE m.email = :email")
    void updatePassword( @Param("password") String password,@Param("email") String email);

    long count();

    @Modifying
    @Query(value = "UPDATE Member m SET m.file_id = :fileId WHERE m.id = :id", nativeQuery = true)
    void updateId(@Param("fileId") Long fileId, @Param("id") Long id);

}