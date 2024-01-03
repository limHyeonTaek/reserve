package com.zerobase.reservation.domain.store.service;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.common.builder.StoreBuilder;
import com.zerobase.reservation.domain.common.utils.KeyGenerator;
import com.zerobase.reservation.domain.member.exception.MemberException;
import com.zerobase.reservation.domain.member.service.MemberService;
import com.zerobase.reservation.domain.store.dto.EditRequest;
import com.zerobase.reservation.domain.store.dto.Location;
import com.zerobase.reservation.domain.store.dto.Registration;
import com.zerobase.reservation.domain.store.dto.StoresResponse;
import com.zerobase.reservation.domain.store.dto.model.AddressDto;
import com.zerobase.reservation.domain.store.dto.model.SalesInfoDto;
import com.zerobase.reservation.domain.store.dto.model.StoreDto;
import com.zerobase.reservation.domain.store.entity.Store;
import com.zerobase.reservation.domain.store.exception.StoreException;
import com.zerobase.reservation.domain.store.repository.StoreRepository;
import com.zerobase.reservation.domain.store.repository.projection.StoreProjection;
import com.zerobase.reservation.general.exception.ApiBadRequestException;
import com.zerobase.reservation.general.infra.address.CoordinateClient;
import com.zerobase.reservation.general.infra.address.dto.CoordinateDto;
import com.zerobase.reservation.general.infra.address.dto.KakaoResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.zerobase.reservation.domain.common.constants.StoreConstants.*;
import static com.zerobase.reservation.general.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock
    StoreRepository storeRepository;

    @Mock
    MemberService memberService;

    @Mock
    KeyGenerator keyGenerator;

    @Mock
    CoordinateClient coordinateClient;

    @InjectMocks
    StoreService storeService;

    @Test
    @DisplayName("매장 등록 성공")
    void registration_success() {
        // given
        given(memberService.findByMemberKeyOrThrow(any()))
                .willReturn(MemberBuilder.member());

        given(storeRepository.save(any()))
                .willReturn(StoreBuilder.store());

        given(keyGenerator.generateKey())
                .willReturn(STORE_KEY);

        given(coordinateClient.getCoordinate(any()))
                .willReturn(getCoordinateDto());

        // when
        StoreDto storeDto = storeService.registration(
                Registration.Request.builder()
                        .memberKey(MEMBER_KEY)
                        .storeName(STORE_NAME)
                        .description(DESCRIPTION)
                        .phoneNumber(PHONE_NUMBER)
                        .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                        .salesInfo(new SalesInfoDto(OPER_START, OPER_END,
                                CLOSED_DAYS))
                        .build()
        );

        // then
        assertEquals(STORE_KEY, storeDto.getStoreKey());
        assertEquals(STORE_NAME, storeDto.getName());
        assertEquals(DESCRIPTION, storeDto.getDescription());
    }

    private static CoordinateDto getCoordinateDto() {
        return new KakaoResponseDto(
                List.of(new KakaoResponseDto.Document(
                        126.921834561892,
                        37.5494881397462))
        );
    }

    @Test
    @DisplayName("매장 등록 실패 - 존재하지 않는 회원")
    void registration_member_not_found() {
        // given
        given(memberService.findByMemberKeyOrThrow(any()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        // when
        MemberException exception = assertThrows(MemberException.class, () ->
                storeService.registration(
                        Registration.Request.builder().build()));

        // then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 등록 실패 - 없는 주소")
    void registration_address_not_found() {
        // given
        given(memberService.findByMemberKeyOrThrow(any()))
                .willReturn(MemberBuilder.member());

        given(coordinateClient.getCoordinate(any()))
                .willReturn(new KakaoResponseDto(Collections.emptyList()));

        // when
        ApiBadRequestException exception =
                assertThrows(ApiBadRequestException.class, () ->
                        storeService.registration(
                                Registration.Request.builder()
                                        .address(new AddressDto(
                                                ADDRESS,
                                                DETAIL_ADDR,
                                                ZIPCODE))
                                        .build()));

        // then
        assertEquals(RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 검색 성공")
    void searchKeyword_success() {
        // given
        String keyword = "초밥";
        List<Store> contents = List.of(
                Store.builder().name("맛있는 초밥").build(),
                Store.builder().name("더 맛있는 초밥집").build(),
                Store.builder().name("이것이 초밥이다").build()
        );
        Pageable limit = PageRequest.of(0, 50);

        given(storeRepository.findAllByNameContains(keyword, limit))
                .willReturn(new PageImpl<>(contents, limit, 3));

        // when
        List<String> storeNames = storeService.searchKeyword(keyword);

        // then
        assertEquals(3, storeNames.size());
    }

    @Test
    @DisplayName("매장 조회 성공")
    void information_success() {
        // given
        Store store = StoreBuilder.store();
        store.addMember(MemberBuilder.member());

        given(storeRepository.findByStoreKey(any()))
                .willReturn(Optional.of(store));

        // when
        StoreDto storeDto = storeService.information(STORE_KEY);

        // then
        assertEquals(STORE_KEY, storeDto.getStoreKey());
    }

    @Test
    @DisplayName("매장 조회 실패 - 존재하지 않는 매장")
    void information_store_not_found() {
        // given
        given(storeRepository.findByStoreKey(any()))
                .willReturn(Optional.empty());

        // when
        StoreException exception = assertThrows(StoreException.class, () ->
                storeService.information(STORE_KEY));

        // then
        assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 목록 조회")
    void stores_success() {
        // given
        StoreProjection projection = new StoreProjection() {
            @Override
            public String getName() {
                return STORE_NAME;
            }

            @Override
            public String getAddress() {
                return ADDRESS;
            }

            @Override
            public Double getScore() {
                return 2.3;
            }
        };

        given(storeRepository.findByDistance(any(), any(), any(), any()))
                .willReturn(new SliceImpl<>(List.of(projection)));

        // when
        Slice<StoresResponse> response =
                storeService.stores(
                        new Location(129.055511349615, 35.1752550133221),
                        PageRequest.of(0, 5)
                );

        // then
        assertEquals(1, response.getSize());
        assertEquals(STORE_NAME, response.getContent().get(0).getStoreName());
    }

    @Test
    @DisplayName("매장 수정 성공")
    void edit_success() {
        // given
        Store store = StoreBuilder.store();
        store.addMember(MemberBuilder.member());

        given(storeRepository.findByStoreKey(any()))
                .willReturn(Optional.of(store));

        given(coordinateClient.getCoordinate(any()))
                .willReturn(getCoordinateDto());

        // when
        StoreDto storeDto = storeService.edit(EditRequest.builder()
                .storeKey(STORE_KEY)
                .storeName("수정된 이름")
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new AddressDto("주소 수정", DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfoDto(LocalTime.MIN, LocalTime.MAX,
                        List.of("일요일")))
                .build()
        );

        // then
        assertEquals(STORE_KEY, storeDto.getStoreKey());
        assertEquals("수정된 이름", storeDto.getName());
        assertEquals("주소 수정", storeDto.getAddress().address());
    }

    @Test
    @DisplayName("매장 수정 실패 - 존재하지 않는 매장")
    void edit_store_not_found() {
        // given
        given(storeRepository.findByStoreKey(any()))
                .willReturn(Optional.empty());

        // when
        StoreException exception = assertThrows(StoreException.class, () ->
                storeService.edit(EditRequest.builder()
                        .storeKey(STORE_KEY)
                        .storeName("수정된 이름")
                        .description(DESCRIPTION)
                        .phoneNumber(PHONE_NUMBER)
                        .address(new AddressDto("주소 수정", DETAIL_ADDR, ZIPCODE))
                        .salesInfo(new SalesInfoDto(LocalTime.MIN, LocalTime.MAX,
                                List.of("일요일")))
                        .build())
        );

        // then
        assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 수정 실패 - 없는 주소")
    void edit_address_not_found() {
        // given
        Store store = StoreBuilder.store();
        store.addMember(MemberBuilder.member());

        given(storeRepository.findByStoreKey(any()))
                .willReturn(Optional.of(store));

        given(coordinateClient.getCoordinate(any()))
                .willReturn(new KakaoResponseDto(Collections.emptyList()));

        // when
        ApiBadRequestException exception =
                assertThrows(ApiBadRequestException.class, () ->
                        storeService.edit(EditRequest.builder()
                                .address(new AddressDto("주소 수정",
                                        DETAIL_ADDR, ZIPCODE))
                                .build())
                );

        // then
        assertEquals(RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 삭제 성공")
    void delete_success() {
        // given
        given(storeRepository.existsByStoreKey(any()))
                .willReturn(true);

        doNothing().when(storeRepository).deleteByStoreKey(any());

        // when
        String result = storeService.delete(STORE_KEY);

        // then
        assertEquals(STORE_KEY, result);
    }

    @Test
    @DisplayName("매장 삭제 실패 - 존재하지 않는 매장")
    void delete_store_not_found() {
        // given
        given(storeRepository.existsByStoreKey(any()))
                .willReturn(false);

        // when
        StoreException exception = assertThrows(StoreException.class, () ->
                storeService.delete(STORE_KEY));

        // then
        assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
    }
}