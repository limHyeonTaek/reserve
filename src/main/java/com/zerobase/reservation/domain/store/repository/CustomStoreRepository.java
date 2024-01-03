package com.zerobase.reservation.domain.store.repository;

public interface CustomStoreRepository {
    /**
     매장 키를 가진 해당 매장을 삭제하는 쿼리
     */
    void deleteByStoreKey(String storeKey);
}
