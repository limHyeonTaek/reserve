package com.zerobase.reservation.domain.reservation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.common.builder.dto.ReservationDtoBuilder;
import com.zerobase.reservation.domain.reservation.dto.Confirm;
import com.zerobase.reservation.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reservation.domain.reservation.dto.Reserve;
import com.zerobase.reservation.domain.reservation.dto.Visit;
import com.zerobase.reservation.domain.reservation.service.ReservationService;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.general.config.SecurityConfig;
import com.zerobase.reservation.general.security.AuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.*;
import static com.zerobase.reservation.domain.common.constants.ReservationConstants.*;
import static com.zerobase.reservation.domain.common.constants.StoreConstants.STORE_KEY;
import static com.zerobase.reservation.domain.common.constants.StoreConstants.STORE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = ReservationController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class,
                SecurityAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                SecurityConfig.class, AuthenticationFilter.class
                        })
        }
)
class ReservationControllerTest {
    @MockBean
    ReservationService reservationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 하기")
    void reserve() throws Exception {
        // given
        given(reservationService.reserve(any()))
                .willReturn(ReservationDtoBuilder.reservationDto());

        // when
        // then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Reserve.Request.builder()
                                        .memberKey(MEMBER_KEY)
                                        .storeKey(STORE_KEY)
                                        .reservationDate(RESERVATION_DATE)
                                        .reservationTime(RESERVATION_TIME)
                                        .persons(PERSONS)
                                        .build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").value(STORE_NAME))
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.reservationDate")
                        .value(RESERVATION_DATE.toString()))
                .andExpect(jsonPath("$.reservationTime")
                        .value(RESERVATION_TIME_STR))
                .andExpect(jsonPath("$.persons").value(PERSONS))
                .andExpect(jsonPath("$.reservationType")
                        .value(RESERVATION_TYPE.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("날짜별 예약 조회")
    void reservations_success() throws Exception {
        // given
        ReservationsResponse response = ReservationsResponse.builder()
                .reservationKey(RESERVATION_KEY)
                .reservationDate(RESERVATION_DATE)
                .reservationTime(RESERVATION_TIME)
                .persons(PERSONS)
                .reservationType(RESERVATION_TYPE)
                .memberName(MEMBER_NAME)
                .memberPhoneNumber(PHONE_NUMBER)
                .build();

        List<ReservationsResponse> contents = List.of(response);

        given(reservationService.reservations(any(), any(), any()))
                .willReturn(new PageImpl<>(contents));

        // when
        // then
        mockMvc.perform(get("/api/reservation")
                .param("storeKey", STORE_KEY)
                .param("reservationDate", "2023-12-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reservationKey")
                        .value(RESERVATION_KEY))
                .andExpect(jsonPath("$.content[0].reservationDate")
                        .value(RESERVATION_DATE.toString()))
                .andExpect(jsonPath("$.content[0].reservationTime")
                        .value(RESERVATION_TIME_STR))
                .andExpect(jsonPath("$.content[0].persons")
                        .value(PERSONS))
                .andExpect(jsonPath("$.content[0].reservationType")
                        .value(RESERVATION_TYPE.name()))
                .andExpect(jsonPath("$.content[0].memberName")
                        .value(MEMBER_NAME))
                .andExpect(jsonPath("$.content[0].memberPhoneNumber")
                        .value(PHONE_NUMBER))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 확인")
    void confirm() throws Exception {
        // given
        given(reservationService.confirm(any()))
                .willReturn(new Confirm.Response(RESERVATION_KEY));

        // when
        // then
        mockMvc.perform(patch("/api/reservation/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new Confirm.Request(RESERVATION_KEY,
                                ReservationType.CONFIRM)
                )))
                .andExpect(jsonPath("$.reservationKey")
                        .value(RESERVATION_KEY))
                .andDo(print());
    }

    @Test
    @DisplayName("방문 확인")
    void visit() throws Exception {
        // given
        given(reservationService.visit(any()))
                .willReturn(new Visit.Response(RESERVATION_KEY));

        // when
        // then
        mockMvc.perform(patch("/api/reservation/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Visit.Request.builder()
                                        .phoneNumber(PHONE_NUMBER)
                                        .storeKey(STORE_KEY)
                                        .reservationDate(RESERVATION_DATE)
                                        .reservationTime(RESERVATION_TIME)
                                        .build()
                        )))
                .andExpect(jsonPath("$.reservationKey")
                        .value(RESERVATION_KEY))
                .andDo(print());
    }
}