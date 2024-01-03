package com.zerobase.reservation.domain.store.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.common.builder.dto.RegistRequestBuilder;
import com.zerobase.reservation.domain.common.builder.dto.StoreDtoBuilder;
import com.zerobase.reservation.domain.store.dto.EditRequest;
import com.zerobase.reservation.domain.store.dto.model.AddressDto;
import com.zerobase.reservation.domain.store.dto.model.SalesInfoDto;
import com.zerobase.reservation.domain.store.service.StoreService;
import com.zerobase.reservation.general.security.TokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.List;

import static com.zerobase.reservation.domain.common.constants.StoreConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@EnableMethodSecurity
@WithMockUser(username = "test", password = "test1234", roles = "MANAGER")
public class StoreControllerSecurityTest {
    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    StoreService storeService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @DisplayName("매니저 권한 - 매장 등록 성공")
    void registration_success() throws Exception {
        // given
        given(storeService.registration(any()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RegistRequestBuilder.registRequest()))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.storeName").value(STORE_NAME))
                .andDo(print());
    }

    @Test
    @DisplayName("매니저 권한 - 매장 수정 성공")
    void edit_success() throws Exception {
        // given
        given(storeService.edit(any()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        EditRequest request = EditRequest.builder()
                .storeKey(STORE_KEY)
                .storeName(STORE_NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfoDto(LocalTime.MIN, LocalTime.MAX,
                        List.of("일요일")))
                .build();

        mockMvc.perform(patch("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.name").value(STORE_NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER))
                .andExpect(jsonPath("$.address.address").value(ADDRESS))
                .andExpect(jsonPath("$.address.detailAddr").value(DETAIL_ADDR))
                .andExpect(jsonPath("$.address.zipcode").value(ZIPCODE))
                .andExpect(jsonPath("$.salesInfo.operatingStart")
                        .value("10:00:00"))
                .andExpect(jsonPath("$.salesInfo.operatingEnd")
                        .value("22:00:00"))
                .andExpect(jsonPath("$.salesInfo.closedDays",
                        Matchers.containsInAnyOrder("일요일")))
                .andDo(print());
    }

    @Test
    @DisplayName("매니저 권한 - 매장 삭제 성공")
    void delete_success() throws Exception {
        // given
        given(storeService.delete(any()))
                .willReturn(STORE_KEY);

        // when
        // then
        mockMvc.perform(delete("/api/stores/" + STORE_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY));
    }
}
