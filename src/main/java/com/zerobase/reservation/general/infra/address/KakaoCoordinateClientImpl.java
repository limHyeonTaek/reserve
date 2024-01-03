package com.zerobase.reservation.general.infra.address;

import com.zerobase.reservation.general.infra.address.dto.CoordinateDto;
import com.zerobase.reservation.general.infra.address.dto.KakaoResponseDto;
import com.zerobase.reservation.general.utils.ObjectMapperUtils;
import com.zerobase.reservation.general.utils.RestTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


/**
 kakao api를 통해 좌표를 가져오는 구현체
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoCoordinateClientImpl implements CoordinateClient {
    @Value("${kakao.key}")
    private String key;
    private static final String URL = "https://dapi.kakao.com/v2/local/search/address";
    private final ObjectMapperUtils objectMapperUtils;
    private final RestTemplateUtils restTemplateUtils;

    @Override
    public CoordinateDto getCoordinate(String address) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("query", address)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, key);

        String result = restTemplateUtils.exchange(uriComponents, headers);
        return objectMapperUtils.jsonToObject(result, KakaoResponseDto.class);
    }
}
