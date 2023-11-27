package com.example.weed.domain.members;

import com.example.weed.dto.MemberFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CheckUsernameValidator extends AbstractValidator<MemberFormDto> {

    private final MemberRepository memberRepository;

    public CheckUsernameValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doValidate(MemberFormDto dto, Errors errors) {
        if (memberRepository.existsByEmail(dto.toEntity().getEmail())) {
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }
    }
}
