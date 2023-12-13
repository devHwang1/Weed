package com.example.weed.service;


import com.example.weed.dto.TodoDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.Todo;
import com.example.weed.repository.TodoRepository;
import com.example.weed.repository.W1001_MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final W1001_MemberRepository w1001_memberRepository;

    private final W1001_MemberService w1001_memberService;

    @Autowired
    public TodoService(TodoRepository todoRepository, W1001_MemberRepository w1001_memberRepository,W1001_MemberService w1001_memberService) {
        this.todoRepository = todoRepository;
        this.w1001_memberRepository = w1001_memberRepository;
        this.w1001_memberService = w1001_memberService;
    }

    public void addTodoList(TodoDTO todoDTO) {
        // 회원 ID로 회원 정보 조회
        // (여기서는 가정으로 회원 ID를 1로 고정)
        Member member =w1001_memberService.getLoggedInMember();

        // Todo 엔티티 생성
        Todo todo = new Todo();
        todo.setTitle(todoDTO.getTitle());
        todo.setContent(todoDTO.getContent());
        todo.setChecked(false); // 초기값은 false로 설정
        todo.setRegistrationTime(new java.sql.Date(new Date().getTime())); // 현재 시간으로 설정
        todo.setMember(member);

        // Todo 엔티티 저장
        todoRepository.save(todo);
    }
    public List<TodoDTO> getTodoList() {
        List<Todo> todoEntities = todoRepository.findAll();
        return todoEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TodoDTO convertToDto(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setContent(todo.getContent());
        dto.setId(todo.getId());
        dto.setChecked(todo.isChecked());
        // 필요에 따라 다른 필드도 매핑
        return dto;
    }

    public void updateTodoStatus(Long todoId, boolean checked) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        todoOptional.ifPresent(todo -> {
            todo.setChecked(checked);
            todoRepository.save(todo);
        });
    }

    public Long getTotalTodoCount() {
        Member member = w1001_memberService.getLoggedInMember();
        return todoRepository.countByMember(member);
    }

    public Long getCheckedTodoCount() {
        Member member = w1001_memberService.getLoggedInMember();
        return todoRepository.countByMemberAndChecked(member, true);
    }


}
