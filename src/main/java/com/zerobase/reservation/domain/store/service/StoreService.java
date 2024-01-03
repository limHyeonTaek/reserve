package com.zerobase.reservation.domain.store.service;

import com.zerobase.reservation.domain.common.utils.KeyGenerator;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.service.MemberService;
import com.zerobase.reservation.domain.store.dto.EditRequest;
import com.zerobase.reservation.domain.store.dto.Registration;
import com.zerobase.reservation.domain.store.dto.Location;
import com.zerobase.reservation.domain.store.dto.StoresResponse;
import com.zerobase.reservation.domain.store.dto.model.AddressDto;
import com.zerobase.reservation.domain.store.dto.model.StoreDto;
import com.zerobase.reservation.domain.store.entity.Address;
import com.zerobase.reservation.domain.store.entity.Store;
import com.zerobase.reservation.domain.store.exception.StoreException;
import com.zerobase.reservation.domain.store.repository.StoreRepository;
import com.zerobase.reservation.domain.store.type.SRID;
import com.zerobase.reservation.general.infra.address.CoordinateClient;
import com.zerobase.reservation.general.infra.address.dto.CoordinateDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.reservation.general.exception.ErrorCode.STORE_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberService memberService;
    private final KeyGenerator keyGenerator;
    private final CoordinateClient coordinateClient;
    private static final String POINT_FORMAT = "POINT(%f %f)";
    private static final Integer RADIUS = 3000;

    public Store findByStoreKeyOrThrow(String storeKey) {
        return storeRepository.findByStoreKey(storeKey)
                .orElseThrow(() -> new StoreException(STORE_NOT_FOUND));
    }

    /**
     매장 등록
     회원 정보를 찾은 후 회원 및 매장 주소에 대한 좌표 데이터를 포함하여 매장 등록을 진행
     */
    @Transactional
    public StoreDto registration(Registration.Request request) {
        Member member = memberService.findByMemberKeyOrThrow(request.getMemberKey());
        CoordinateDto coordinate = getCoordinate(request.getAddress().address());

        Store store = request.toEntity(
                keyGenerator.generateKey(),
                coordinate.getX(), coordinate.getY()
        );

        store.addMember(member);
        storeRepository.save(store);
        return StoreDto.fromEntity(store);
    }

    private CoordinateDto getCoordinate(String address) {
        return coordinateClient.getCoordinate(address);
    }

    /**
     매장명 자동완성
     */
    public List<String> searchKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 50);
        return storeRepository.findAllByNameContains(keyword, limit)
                .stream()
                .map(Store::getName)
                .toList();
    }

    /**
     매장 정보 조회
     */
    public StoreDto information(String storeKey) {
        return StoreDto.fromEntity(findByStoreKeyOrThrow(storeKey));
    }

    /**
     매장 목록 조회
     클라이언트 위치 기반 반경 3km 이내에 있는 매장을 조회
     */
    public Slice<StoresResponse> stores(Location location, Pageable pageable) {
        return storeRepository.findByDistance(
                        String.format(POINT_FORMAT, location.y(), location.x()),
                        SRID.POINT.getKey(),
                        RADIUS,
                        pageable)
                .map(StoresResponse::from);
    }

    /**
     매장 수정
     매장 주소 변경 시 위치 정보를 포함하여 매장 수정을 진행
     */
    @Transactional
    public StoreDto edit(EditRequest request) {
        Store store = findByStoreKeyOrThrow(request.getStoreKey());

        Address savedAddress = store.getAddress();
        Point coordinate = savedAddress.getCoordinate();
        Double x = coordinate.getX();
        Double y = coordinate.getY();

        String requestAddr = request.getAddress().address();

        if (!requestAddr.equals(savedAddress.getAddress())) {
            CoordinateDto newCoordinate = getCoordinate(requestAddr);
            x = newCoordinate.getX();
            y = newCoordinate.getY();
        }

        store.updateStore(request,
                AddressDto.toEntity(request.getAddress(), x, y));
        return StoreDto.fromEntity(store);
    }

    /**
     매장 삭제
     매장 검증 후 삭제를 진행
     */
    @Transactional
    public String delete(String storeKey) {
        validateStoreExists(storeKey);
        storeRepository.deleteByStoreKey(storeKey);
        return storeKey;
    }

    private void validateStoreExists(String storeKey) {
        if (!storeRepository.existsByStoreKey(storeKey)) {
            throw new StoreException(STORE_NOT_FOUND);
        }
    }
}
