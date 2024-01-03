package com.zerobase.reservation.domain.member.service;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.common.utils.KeyGenerator;
import com.zerobase.reservation.domain.member.dto.Signin;
import com.zerobase.reservation.domain.member.dto.Signup;
import com.zerobase.reservation.domain.member.dto.model.MemberDto;
import com.zerobase.reservation.domain.member.entity.Role;
import com.zerobase.reservation.domain.member.exception.MemberException;
import com.zerobase.reservation.domain.member.repository.MemberRepository;
import com.zerobase.reservation.general.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.*;
import static com.zerobase.reservation.domain.common.constants.StoreConstants.MEMBER_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    KeyGenerator keyGenerator;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        given(memberRepository.save(any()))
                .willReturn(MemberBuilder.member());

        given(keyGenerator.generateKey())
                .willReturn(MEMBER_KEY);

        given(passwordEncoder.encode(any()))
                .willReturn(PASSWORD);

        // when
        MemberDto memberDto = authService.signup(Signup.builder().build());

        // then
        assertEquals(MEMBER_KEY, memberDto.getMemberKey());
        assertEquals(MEMBER_NAME, memberDto.getName());
        assertEquals(EMAIL, memberDto.getEmail());
        assertEquals(Role.USER, memberDto.getRole());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 있는 회원")
    void signup_member_already_exists() {
        // given
        given(memberRepository.existsByEmail(any()))
                .willReturn(true);

        // when
        MemberException exception = assertThrows(MemberException.class, () ->
                authService.signup(Signup.builder().build()));

        // then
        assertEquals(ErrorCode.MEMBER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(MemberBuilder.member());

        given(authenticationManagerBuilder.getObject())
                .willReturn(mock(AuthenticationManager.class));

        given(authenticationManagerBuilder.getObject().authenticate(any()))
                .willReturn(authentication);

        // when
        MemberDto memberDto = authService.signin(new Signin(EMAIL, PASSWORD));

        // then
        assertEquals(MEMBER_NAME, memberDto.getName());
        assertEquals(EMAIL, memberDto.getEmail());
        assertEquals(Role.USER, memberDto.getRole());
    }
}