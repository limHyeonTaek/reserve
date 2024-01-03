package com.zerobase.reservation.domain.member.dto;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.entity.Role;
import com.zerobase.reservation.domain.common.validator.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signup {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z1-9]{8,16}",
            message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 입니다.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "핸드폰 번호의 약식과 맞지 않습니다. ex) 010-1234-1234")
    private String phoneNumber;

    @ValidEnum(enumClass = Role.class)
    private Role role;

    public Member toEntity(String memberKey, String password) {
        return Member.builder()
                .memberKey(memberKey)
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }
}
