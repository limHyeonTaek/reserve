package com.zerobase.reservation.domain.common.constants;

import java.time.LocalTime;
import java.util.List;

public class StoreConstants {
    public static final String MEMBER_KEY = "b95c5f9e34da4cf9a394955a328775f5";
    public static final String STORE_KEY = "34d95da5f3a6401bb070834306171e94";
    public static final String STORE_NAME = "매장 A";
    public static final String DESCRIPTION = "매장 설명";
    public static final String PHONE_NUMBER = "02-123-1234";
    public static final String ADDRESS = "서울시 자치구 도로명";
    public static final String DETAIL_ADDR = "상세 주소";
    public static final String ZIPCODE = "123-4";
    public static final LocalTime OPER_START = LocalTime.of(10, 0);
    public static final LocalTime OPER_END = LocalTime.of(22, 0);
    public static final List<String> CLOSED_DAYS = List.of("일요일");
}
