package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.CreateDressRequest;
import com.thanh_phoi_co.dto.request.UpdateDressRequest;
import com.thanh_phoi_co.dto.response.DressResponse;
import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.enums.DressStatus;
import com.thanh_phoi_co.service.DressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dresses")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dress Controller")
public class DressController {

    private final DressService dressService;

    @Operation(summary = "Tạo váy mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<DressResponse> createDress(@Valid @RequestBody CreateDressRequest request) {
        DressResponse response = dressService.createDress(request);
        return ResponseData.<DressResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Cập nhật váy")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<DressResponse> updateDress(@PathVariable String id, @Valid @RequestBody UpdateDressRequest request) {
        DressResponse response = dressService.updateDress(id, request);
        return ResponseData.<DressResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Xóa váy")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<String> deleteDress(@PathVariable String id) {
        dressService.deleteDress(id);
        return ResponseData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Xóa váy thành công")
                .build();
    }

    @Operation(summary = "Lấy thông tin váy theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseData<DressResponse> getDressById(@PathVariable String id) {
        DressResponse response = dressService.getDressById(id);
        return ResponseData.<DressResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lấy danh sách tất cả váy")
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseData<Page<DressResponse>> getAllDresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DressResponse> response = dressService.getAllDresses(pageable);
        return ResponseData.<Page<DressResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Tìm kiếm váy theo từ khóa")
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseData<Page<DressResponse>> searchDresses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DressResponse> response = dressService.searchDresses(keyword, pageable);
        return ResponseData.<Page<DressResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lọc váy")
    @GetMapping("/filter")
    @PreAuthorize("permitAll()")
    public ResponseData<Page<DressResponse>> filterDresses(
            @RequestParam(required = false) DressStatus status,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<DressResponse> response = dressService.filterDresses(status, categoryId, color, size, pageable);
        return ResponseData.<Page<DressResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lọc váy thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lấy danh sách màu sắc")
    @PreAuthorize("permitAll()")
    @GetMapping("/colors")
    public ResponseData<List<String>> getAllColors() {
        List<String> colors = dressService.getAllColors();
        return ResponseData.<List<String>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách màu sắc thành công")
                .data(colors)
                .build();
    }

    @Operation(summary = "Lấy danh sách kích cỡ")
    @PreAuthorize("permitAll()")
    @GetMapping("/sizes")
    public ResponseData<List<String>> getAllSizes() {
        List<String> sizes = dressService.getAllSizes();
        return ResponseData.<List<String>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách kích cỡ thành công")
                .data(sizes)
                .build();
    }
}
