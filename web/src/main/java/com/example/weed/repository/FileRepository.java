package com.example.weed.repository;

import com.example.weed.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    File findByFileName(String fileName);

    File findByMemberId(Long id);
}
