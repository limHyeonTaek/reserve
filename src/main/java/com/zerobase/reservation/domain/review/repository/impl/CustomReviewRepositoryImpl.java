package com.zerobase.reservation.domain.review.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reservation.domain.member.entity.QMember;
import com.zerobase.reservation.domain.review.entity.Review;
import com.zerobase.reservation.domain.review.repository.CustomReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.zerobase.reservation.domain.member.entity.QMember.member;
import static com.zerobase.reservation.domain.review.entity.QReview.review;
import static com.zerobase.reservation.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Review> findByIdFetchJoin(Long id) {

        return Optional.ofNullable(queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .join(review.store, store).fetchJoin()
                .join(review.store.member, new QMember("manager"))
                .fetchJoin()
                .where(review.id.eq(id))
                .fetchOne());
    }

    @Override
    public void deleteByReviewId(Long id) {
        queryFactory
                .delete(review)
                .where(review.id.eq(id))
                .execute();
    }
}
