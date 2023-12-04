package com.example.weed.service;


import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.repository.FileRepository;
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
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @Override
    public File findByFileName(String fileName) {
        return fileRepository.findByFileName(fileName);
    }

    @Override
    public void save(File file) {
        fileRepository.save(file);
    }

    @Override
    @Transactional
    public List<File> uploadFile(MultipartFile[] uploadFiles, Member loggedInMember) {
        for (MultipartFile uploadFile : uploadFiles) {
            if (!uploadFile.getContentType().startsWith("image")) {
                return null;
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = Paths.get(originalName).getFileName().toString();
            String folderPath = makeFolder();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + java.io.File.separator + folderPath + java.io.File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);

                File existingFile = findByFileName(fileName);
                File memberId = fileRepository.findByMemberId(loggedInMember.getId());
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
