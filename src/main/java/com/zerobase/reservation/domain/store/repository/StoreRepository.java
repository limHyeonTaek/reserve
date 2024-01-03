package com.zerobase.reservation.domain.store.repository;

import com.zerobase.reservation.domain.store.entity.Store;
import com.zerobase.reservation.domain.store.repository.projection.StoreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
    Page<Store> findAllByNameContains(String name, Pageable pageable);

    Optional<Store> findByStoreKey(String storeKey);

    boolean existsByStoreKey(String storeKey);

    /**
     입력 받은 위치 기반 반경 3km 이내의 매장을 조회하는 쿼리
     @param point    클라이언트 위치 좌표
     @param srid     지리 데이터를 사용하기 위한 식별 id
     @param radius   반경
     @param pageable 페이징
     @return 지정된 프로젝션
     */
    @Query("select s.name as name, s.address.address as address, avg(r.score) as score " +
            "from Store s left join Review r on s.id = r.store.id " +
            "where ST_Distance_Sphere(ST_GeomFromText(:point, :srid), s.address.coordinate) <= :redius " +
            "group by s.id")
    Slice<StoreProjection> findByDistance(@Param("point") String point,
                                          @Param("srid") Integer srid,
                                          @Param("redius") Integer radius,
                                          Pageable pageable);
}
