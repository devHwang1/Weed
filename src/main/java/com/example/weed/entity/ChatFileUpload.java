package com.example.weed.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다양한 필드들을 추가할 수 있습니다.
    private String originFileName;

    private String transaction;

    private String s3DataUrl;

    private String fileDir;

    // 예시로 ManyToOne 관계 설정. 파일이 속한 채팅방 정보를 관리하려면 해당 필드를 추가하고 연결해야 합니다.
    @ManyToOne
    private ChatRoom chatRoom;

    // 기타 다른 필드 및 로직 추가 가능
}
