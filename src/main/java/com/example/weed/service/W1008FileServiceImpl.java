package com.example.weed.service;


import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1008_FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class W1008FileServiceImpl implements W1008_FileService {

    @Autowired
    private W1008_FileRepository w1008FileRepository;

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @Override
    public File findByFileName(String fileName) {
        return w1008FileRepository.findByFileName(fileName);
    }

    @Override
    public void save(File file) {
        w1008FileRepository.save(file);
    }

    @Override
    @Transactional
    public List<File> uploadFile(MultipartFile[] uploadFiles, Member loggedInMember) {
        for (MultipartFile uploadFile : uploadFiles) {
            if (!uploadFile.getContentType().startsWith("image")) {
                return null;
            }

            String originalName = uploadFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + Paths.get(originalName).getFileName().toString();
//            String folderPath = makeFolder();
            String saveName = uploadPath + java.io.File.separator  + java.io.File.separator + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);

                File existingFile = findByFileName(fileName);
                File memberId = w1008FileRepository.findByMemberId(loggedInMember.getId());
                if (loggedInMember.getFile() == null || memberId == null) {
                    File newFile = new File();
                    newFile.setFileName(fileName);
                    newFile.setFilePath(saveName);
                    newFile.setMember(loggedInMember);
                    if(loggedInMember.getId() != null){
                        newFile.setId(loggedInMember.getId());
                    }

                    save(newFile);
                } else {
                    memberId.setFilePath(saveName);
                    memberId.setFileName(fileName);
                    save(memberId);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", java.io.File.separator);

        Path uploadPathFolder = Paths.get(uploadPath, folderPath);

        if (!Files.exists(uploadPathFolder)) {
            try {
                Files.createDirectories(uploadPathFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folderPath;
    }
}
