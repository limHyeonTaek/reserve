package com.zerobase.reservation.domain.store.dto;

import com.zerobase.reservation.domain.store.repository.projection.StoreProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoresResponse {
    private String storeName;
    private String address;
    private Double score;

    public static StoresResponse from(StoreProjection projection) {
        Double score = projection.getScore();
        if (score == null) {
            score = 0.0;
        }

        return StoresResponse.builder()
                .storeName(projection.getName())
                .address(projection.getAddress())
                .score(score)
                .build();
    }
}
