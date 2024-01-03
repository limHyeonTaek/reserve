package com.zerobase.reservation.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Signin(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "[a-zA-Z1-9]{8,16}",
                message = "비밀번호를 바르게 입력해 주세요.")
        String password
) {
}
