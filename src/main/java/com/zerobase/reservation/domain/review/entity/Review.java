package com.zerobase.reservation.domain.review.entity;

import com.zerobase.reservation.domain.common.entity.BaseTimeEntity;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.review.dto.Update;
import com.zerobase.reservation.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contents;

    @ColumnDefault("0.0")
    private Double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Review(String contents, Double score) {
        this.contents = contents;
        this.score = score;
    }

    public void addMemberAndStore(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void updateReview(Update.Request request) {
        this.contents = request.getContents();
        this.score = request.getScore();
    }
 }
