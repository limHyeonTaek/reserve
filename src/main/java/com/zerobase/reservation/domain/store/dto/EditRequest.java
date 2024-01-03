package com.zerobase.reservation.domain.store.dto;

import com.zerobase.reservation.domain.store.dto.model.AddressDto;
import com.zerobase.reservation.domain.store.dto.model.SalesInfoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRequest {
    @NotBlank
    private String storeKey;

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
}
