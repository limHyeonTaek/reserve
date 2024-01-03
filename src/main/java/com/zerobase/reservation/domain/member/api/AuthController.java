package com.zerobase.reservation.domain.member.api;

import com.zerobase.reservation.domain.member.dto.Signin;
import com.zerobase.reservation.domain.member.service.AuthService;
import com.zerobase.reservation.domain.member.dto.model.MemberDto;
import com.zerobase.reservation.domain.member.dto.Signup;
import com.zerobase.reservation.general.security.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 회원가입, 로그인 컨트롤러
 인증 없이 접근할 수 있습니다.
 */
@RequestMapping("/api/login")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    /**
     회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody @Valid Signup request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    /**
     로그인
     로그인 완료 후 해당 정보로 토큰을 발급합니다.
     */
    @PostMapping("/signin")
    public ResponseEntity<MemberDto> signin(@RequestBody @Valid Signin request) {
        MemberDto memberDto = authService.signin(request);
        String token = tokenProvider.generateToken(memberDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(memberDto);
    }
}
