package com.zerobase.reservation.general.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.general.exception.ApiBadRequestException;
import com.zerobase.reservation.general.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 외부 api와 통신한 결과값을 매핑하는 범용 유틸 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ObjectMapperUtils {
    private final ObjectMapper objectMapper;

    public <T> T jsonToObject(String json, Class<T> tClass) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }

        try {
            objectMapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException is occurred. ", e);
            throw new ApiBadRequestException(ErrorCode.INTERNAL_ERROR,
                    "객체 매핑에 문제가 발생했습니다.");
        }
    }
}
