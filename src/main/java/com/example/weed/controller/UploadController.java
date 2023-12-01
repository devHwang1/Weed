package com.example.weed.controller;

import com.example.weed.dto.CustomDetails;
import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.service.FileService;
import com.example.weed.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UploadController {



    @Autowired
    private FileService fileService;

    @Autowired
    private MemberService memberService;

    @PostMapping("/uploadAjax")
    public ResponseEntity<String> uploadFile(@RequestPart("uploadFiles") MultipartFile[] uploadFiles) {
        try {
            Member loggedInMember = memberService.getLoggedInMember();
            fileService.uploadFile(uploadFiles,loggedInMember);
            memberService.updateMemberFiles(loggedInMember);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




