package com.zerobase.reservation.domain.store.entity;

import com.zerobase.reservation.domain.common.entity.BaseTimeEntity;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.store.dto.EditRequest;
import com.zerobase.reservation.domain.store.dto.model.SalesInfoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(columnList = "store_key")})
@Entity
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "store_key")
    private String storeKey;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Embedded
    private SalesInfo salesInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Store(String storeKey, String name, String description, String phoneNumber, Address address, SalesInfo salesInfo) {
        this.storeKey = storeKey;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.salesInfo = salesInfo;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void updateStore(EditRequest request, Address address) {
        this.name = request.getStoreName();
        this.description = request.getDescription();
        this.phoneNumber = request.getPhoneNumber();
        this.address = address;
        this.salesInfo = SalesInfoDto.toEntity(request.getSalesInfo());
    }
}
