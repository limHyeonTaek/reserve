package com.zerobase.reservation.general.infra.address;

import com.zerobase.reservation.general.infra.address.dto.CoordinateDto;

/**
 주소 기반 좌표를 가져오는 범용 인터페이스
 */
public interface CoordinateClient {
    CoordinateDto getCoordinate(String address);
}
