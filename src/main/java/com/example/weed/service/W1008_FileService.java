package com.example.weed.service;

import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface W1008_FileService {

    File findByFileName(String fileName);

    void save(File file);

    List<File> uploadFile(MultipartFile[] uploadFiles, Member loggedInMember);
}
