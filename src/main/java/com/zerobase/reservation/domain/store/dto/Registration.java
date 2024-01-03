package com.zerobase.reservation.domain.store.dto;

import com.zerobase.reservation.domain.store.dto.model.AddressDto;
import com.zerobase.reservation.domain.store.dto.model.SalesInfoDto;
import com.zerobase.reservation.domain.store.dto.model.StoreDto;
import com.zerobase.reservation.domain.store.entity.Store;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Registration {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String memberKey;

        @NotBlank
        private String storeName;

        @NotBlank
        private String description;

        @NotBlank
        @Pattern(regexp = "^\\d{2}-\\d{3}-\\d{4}$",
                message = "전화번호의 약식과 맞지 않습니다. ex) 02-123-1234")
        private String phoneNumber;

        @Valid
        @NotNull
        private AddressDto address;

        @Valid
        @NotNull
        private SalesInfoDto salesInfo;

        public Store toEntity(String storeKey, Double x, Double y) {
            return Store.builder()
                    .storeKey(storeKey)
                    .name(storeName)
                    .description(description)
                    .phoneNumber(phoneNumber)
                    .address(AddressDto.toEntity(address, x, y))
                    .salesInfo(SalesInfoDto.toEntity(salesInfo))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String storeKey;
        private String storeName;

        public static Response from(StoreDto storeDto) {
            return Response.builder()
                    .storeKey(storeDto.getStoreKey())
                    .storeName(storeDto.getName())
                    .build();
        }
    }
}
