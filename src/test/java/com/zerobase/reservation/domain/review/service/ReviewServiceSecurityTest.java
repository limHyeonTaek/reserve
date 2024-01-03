package com.zerobase.reservation.domain.review.service;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.common.builder.StoreBuilder;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.service.MemberService;
import com.zerobase.reservation.domain.reservation.service.ReservationService;
import com.zerobase.reservation.domain.review.dto.Update;
import com.zerobase.reservation.domain.review.dto.model.ReviewDto;
import com.zerobase.reservation.domain.review.entity.Review;
import com.zerobase.reservation.domain.review.repository.ReviewRepository;
import com.zerobase.reservation.domain.store.entity.Store;
import com.zerobase.reservation.general.config.SecurityConfig;
import com.zerobase.reservation.general.security.AuthenticationFilter;
import com.zerobase.reservation.general.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.EMAIL;
import static com.zerobase.reservation.domain.common.constants.ReviewConstants.ID;
import static com.zerobase.reservation.domain.common.constants.ReviewConstants.SCORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {
        SecurityConfig.class, AuthenticationFilter.class, TokenProvider.class,
        ReviewService.class
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@EnableMethodSecurity
public class ReviewServiceSecurityTest {
    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    ReservationService reservationService;

    @MockBean
    MemberService memberService;

    @Autowired
    ReviewService reviewService;

    @Test
    @DisplayName("리뷰 수정 성공 - 동일한 유저")
    @WithMockUser(username = EMAIL, roles = "USER")
    void update_success_same_user() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();
        String contents = "맛있어요";

        Review review = Review.builder()
                .contents(contents)
                .score(SCORE)
                .build();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        // when
        ReviewDto reviewDto = reviewService.update(
                Update.Request.builder()
                        .reviewId(ID)
                        .contents(contents)
                        .score(SCORE)
                        .build());

        // then
        assertEquals(contents, reviewDto.getContents());
        assertEquals(SCORE, reviewDto.getScore());
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 서로 다른 유저")
    @WithMockUser(username = "test2@gmail.com", roles = "USER")
    void update_not_equal_user() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();
        String contents = "맛있어요";

        Review review = Review.builder()
                .contents(contents)
                .score(SCORE)
                .build();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        // when
        // then
        assertThrows(AccessDeniedException.class, () ->
                reviewService.update(Update.Request.builder()
                                .reviewId(ID)
                                .contents(contents)
                                .score(SCORE)
                                .build()));
    }
}
