package com.zerobase.reservation.domain.store.api;

import com.zerobase.reservation.domain.store.dto.*;
import com.zerobase.reservation.domain.store.dto.model.StoreDto;
import com.zerobase.reservation.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    /**
     매장 등록
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Registration.Response> registration(
            @RequestBody @Valid Registration.Request request) {
        StoreDto storeDto = storeService.registration(request);
        return ResponseEntity.ok(
                Registration.Response.from(storeDto));
    }

    /**
     매장명 자동완성
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchKeyword(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                new SearchResponse(storeService.searchKeyword(keyword)));
    }

    /**
     매장 정보 조회
     */
    @GetMapping("/{storeKey}")
    public ResponseEntity<StoreDto> information(@PathVariable String storeKey) {
        return ResponseEntity.ok(storeService.information(storeKey));
    }

    /**
     매장 목록 조회 - 반경 3km 이내
     */
    @GetMapping
    public ResponseEntity<Slice<StoresResponse>> stores(
            @Valid Location location, final Pageable pageable) {
        return ResponseEntity.ok(storeService.stores(location, pageable));
    }

    /**
     매장 수정
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping
    public ResponseEntity<StoreDto> edit(@RequestBody @Valid EditRequest request) {
        return ResponseEntity.ok(storeService.edit(request));
    }

    /**
     매장 삭제
     */
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{storeKey}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable String storeKey) {
        return ResponseEntity.ok(
                new DeleteResponse(storeService.delete(storeKey))
        );
    }
}
