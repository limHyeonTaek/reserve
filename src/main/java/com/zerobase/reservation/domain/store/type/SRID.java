package com.zerobase.reservation.domain.store.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SRID {
    POINT(4326);

    private final Integer key;
}
