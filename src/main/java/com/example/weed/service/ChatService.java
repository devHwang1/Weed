package com.example.weed.service;

import com.example.weed.dto.ChatRoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void  init() {
        chatRoomDTOMap = new LinkedHashMap<>();
    }
    //전체 채팅방 조회
    public List<ChatRoomDTO> findAllRoom() {
        //채팅방 생성 순서를 최신순 으로 반환
        List chatRooms = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    //roomID 기준으로 채팅방 찾기
    public ChatRoomDTO findRoomById(String roomId){
        return chatRoomDTOMap.get(roomId);
    }
    //roomName 으로 채팅방 만들기
    public ChatRoomDTO createRoom(String roomName, String roomPwd, boolean secretChk, int maxUserCnt) {

        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .roomPwd(roomPwd)
                .secretChk(secretChk)
                .userList(new HashMap<String, String>())
                .roomUserCount(0)
                .maxUserCout(maxUserCnt)
                .build();

        //map 에 채팅 룸 아이디와 만들어진 채팅룸을 저장함
        chatRoomDTOMap.put(chatRoomDTO.getRoomId(), chatRoomDTO);

        return chatRoomDTO;

    }

    // 채팅방에 인원수 추가
    public void addCountUser(String roomId) {
        //채팅방 만든후 chatRoomDTOMap 저장되어있는 roomId 들고옴
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        chatRoom.setRoomUserCount(chatRoom.getRoomUserCount()+1);
    }

    // 채팅방 인원수 제거
    public void disCountUser(String roomId) {
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        chatRoom.setRoomUserCount(chatRoom.getRoomUserCount()-1);
    }

    // maxUserCnt 에 따른 채팅방 입장여부 확인
    public boolean checkRoomUserCnt(String roomId) {
        ChatRoomDTO chatRoomDTO = chatRoomDTOMap.get(roomId);
        log.info("참여 인원 확인 [{}, {}]", chatRoomDTO.getRoomUserCount(), chatRoomDTO.getMaxUserCout());

        if (chatRoomDTO.getRoomUserCount() + 1 > chatRoomDTO.getMaxUserCout()) {
            return false;
        }

        return true;
    }

    // 채팅방 유저리스트에 유저 추가
    public String addUserList(String roomId, String userName) {
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        //아이디 중복확인 후 userList 에 추가
        //ChatRoomDTO 에 있는 HashMap userList 에서 저장 .getUserList()
        chatRoom.getUserList().put(userUUID, userName);

        return userUUID;
    }

    //채팅방 유저 이름 중복확인
    public String isDuplicateName(String roomId, String userName) {
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        String ranUserNum = userName;

        //만약 userName 이 중복이면? 이름에 랜덤숫자붙임
        // 숫자를 붙였는데 getUserList 안에 또 있으면 또 랜덤붙임

        while (chatRoom.getUserList().containsValue(ranUserNum)) {
            int ranNum = (int) (Math.random() * 100) + 1;

            ranUserNum = userName + ranNum;
        }
        return ranUserNum;
    }

    //채팅방 유저 리스트 삭제
    public void deleteUser(String roomId, String userUUID) {
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        chatRoom.getUserList().remove(userUUID);
    }

    //채팅방 userName조회
    public String getUserName(String roomId, String userUUID) {
        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        return chatRoom.getUserList().get(userUUID);
    }

    //채팅방 전체 userLiset 조회
    public ArrayList<String> getUserList(String roomId) {
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDTO chatRoom = chatRoomDTOMap.get(roomId);
        //hashmap 을 for문으로 돌림
        //그 후에 value 값만 가져와서 list에 저장후 return

        chatRoom.getUserList().forEach((key, value) -> list.add(value));
        return list;
    }

    //채팅방 비밀번호 조회
    public boolean confirmPwd(String roomId, String roomPwd) {
        return roomPwd.equals(chatRoomDTOMap.get(roomId).getRoomPwd());
    }

    //채팅방 삭제
    public void deleteChatRoom(String roomId) {
        try {
            //채팅방 삭세
            chatRoomDTOMap.remove(roomId);

            log.info("삭제완료 roomId : {} ", roomId);
        } catch (Exception e) {
            //예외 발생 확인하기우해서
            System.out.println(e.getMessage());
        }
    }


}
