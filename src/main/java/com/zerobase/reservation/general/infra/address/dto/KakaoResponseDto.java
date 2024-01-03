package com.zerobase.reservation.general.infra.address.dto;

import com.zerobase.reservation.general.exception.ApiBadRequestException;
import com.zerobase.reservation.general.exception.ErrorCode;

import java.util.List;

public record KakaoResponseDto(List<Document> documents) implements CoordinateDto {
    public Document document() {
        if (documents.isEmpty()) {
            throw new ApiBadRequestException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return documents.get(0);
    }

    public record Document(
            Double x, // 경도
            Double y // 위도
    ) {}

    @Override
    public Double getX() {
        return document().x;
    }

    @Override
    public Double getY() {
        return document().y;
    }
}
