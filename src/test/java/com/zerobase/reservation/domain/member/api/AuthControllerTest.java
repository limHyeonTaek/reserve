package com.zerobase.reservation.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.common.builder.dto.MemberDtoBuilder;
import com.zerobase.reservation.domain.member.dto.Signin;
import com.zerobase.reservation.domain.member.dto.Signup;
import com.zerobase.reservation.domain.member.entity.Role;
import com.zerobase.reservation.domain.member.service.AuthService;
import com.zerobase.reservation.general.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class AuthControllerTest {
    @MockBean
    AuthService authService;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(MEMBER_NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role")
                        .value("USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 이메일 형식")
    void signup_invalid_email() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(MEMBER_NAME)
                .email("gmail.com")
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 비밀번호 형식")
    void signup_invalid_password() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password("1111")
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 전화번호 형식")
    void signup_invalid_phoneNumber() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber("1234-1234")
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 enum 형식")
    void signup_invalid_role() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .build();

        mockMvc.perform(post("/api/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() throws Exception {
        // given
        given(authService.signin(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        given(tokenProvider.generateToken(any()))
                .willReturn("");

        // when
        // then
        Signin signin = new Signin(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/login/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(MEMBER_NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role")
                        .value("USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 부적절한 이메일 형식")
    void signin_invalid_email() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signin signin = new Signin("gmail.com", PASSWORD);

        mockMvc.perform(post("/api/login/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 부적절한 비밀번호 형식")
    void signin_invalid_password() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(MemberDtoBuilder.memberDto());

        // when
        // then
        Signin signin = new Signin(EMAIL, "1234");

        mockMvc.perform(post("/api/login/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}