package com.zerobase.reservation.domain.member.service;

import com.zerobase.reservation.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.zerobase.reservation.general.exception.ErrorCode.MEMBER_NOT_FOUND;

/**
 Spring security의 UserDetailsService를 구현한 구현체
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     authenticate 메소드가 실행되면, 해당 메소드를 호출하여
     유저 검증 후 인증된 유저 객체를 리턴
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MEMBER_NOT_FOUND.getMessage()));
    }
}
