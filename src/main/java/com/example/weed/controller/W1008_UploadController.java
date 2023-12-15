package com.example.weed.controller;

import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.service.W1008_FileService;
import com.example.weed.service.W1001_MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.file.Files;


@RestController
public class W1008_UploadController {

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private W1008_FileService w1008FileService;

    @Autowired
    private W1001_MemberService w1001MemberService;

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<String> uploadFile(@RequestPart("uploadFiles") MultipartFile[] uploadFiles) {

        try {
            Member loggedInMember = w1001MemberService.getLoggedInMember();
            w1008FileService.uploadFile(uploadFiles,loggedInMember);
            w1001MemberService.updateMemberFiles(loggedInMember);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            java.io.File file = new java.io.File(uploadPath + java.io.File.separator +
                    srcFileName);
            HttpHeaders header = new HttpHeaders();

            // MIME타입 처리
            header.add("Content-Type",
                    Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
                    header, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @GetMapping("/getProfileImageName")
    public ResponseEntity<String> getProfileImageName() {
        Member loggedInMember = w1001MemberService.getLoggedInMember();
        if (loggedInMember != null) {
            File file = loggedInMember.getFile();
            if (file != null) {
                String fileName = file.getFileName(); // 파일 엔터티에서 파일명을 가져오는 로직으로 수정
                return ResponseEntity.ok(fileName);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile image not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @PostMapping("/updateNameAndPassword")
    @ResponseBody
    public String updateEmailAndPassword(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "password", required = false) String password) {
        Member loggedInMember = w1001MemberService.getLoggedInMember();
        if (loggedInMember != null) {
            // 이름 또는 비밀번호가 제공되었는지 확인하고 해당 필드를 업데이트합니다.
            if (name != null && !name.isEmpty()) {
                loggedInMember.setName(name);
            }
            if (password != null && !password.isEmpty()) {
                loggedInMember.setPassword(passwordEncoder.encode(password));
            }

            // 이름 또는 비밀번호 중 하나라도 변경된 경우에만 저장합니다.
            if (name != null || password != null) {
                // 업데이트된 멤버를 저장합니다.
                w1001MemberService.saveMember(loggedInMember);
                return "Success";  // 성공적으로 업데이트됨
            } else {
                return "NoChanges";  // 변경된 내용이 없음
            }
        } else {
            return "Error";  // 사용자 정보를 찾을 수 없음
        }
    }


}




