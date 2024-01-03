package com.zerobase.reservation.domain.store.dto.model;

import com.zerobase.reservation.domain.store.entity.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank
        String address,

        @NotBlank
        String detailAddr,

        @NotBlank
        String zipcode
) {
        public static Address toEntity(AddressDto addressDto, Double x, Double y) {
                Address address = Address.builder()
                        .address(addressDto.address)
                        .detailAddr(addressDto.detailAddr)
                        .zipcode(addressDto.zipcode)
                        .build();

                address.addCoordinate(x, y);
                return address;
        }

        public static AddressDto fromEntity(Address addressEntity) {
                return new AddressDto(addressEntity.getAddress(),
                        addressEntity.getDetailAddr(),
                        addressEntity.getZipcode());
        }
}
