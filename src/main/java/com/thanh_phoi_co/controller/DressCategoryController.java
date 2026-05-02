package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.DressCategoryRequest;
import com.thanh_phoi_co.dto.response.DressCategoryResponse;
import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.service.DressCategoryService;
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
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dress Category Controller")
public class DressCategoryController {

    private final DressCategoryService dressCategoryService;

    @Operation(summary = "Tạo danh mục váy mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<DressCategoryResponse> createCategory(@Valid @RequestBody DressCategoryRequest request) {
        DressCategoryResponse response = dressCategoryService.createCategory(request);
        return ResponseData.<DressCategoryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo danh mục thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Cập nhật danh mục váy")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<DressCategoryResponse> updateCategory(@PathVariable String id, @Valid @RequestBody DressCategoryRequest request) {
        DressCategoryResponse response = dressCategoryService.updateCategory(id, request);
        return ResponseData.<DressCategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật danh mục thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Xóa danh mục váy")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<String> deleteCategory(@PathVariable String id) {
        dressCategoryService.deleteCategory(id);
        return ResponseData.<String>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa danh mục thành công")
                .build();
    }

    @Operation(summary = "Lấy thông tin danh mục theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseData<DressCategoryResponse> getCategoryById(@PathVariable String id) {
        DressCategoryResponse response = dressCategoryService.getCategoryById(id);
        return ResponseData.<DressCategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin danh mục thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lấy danh sách tất cả danh mục")
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseData<Page<DressCategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DressCategoryResponse> response = dressCategoryService.getAllCategories(pageable);
        return ResponseData.<Page<DressCategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách danh mục thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lấy danh sách tất cả danh mục (không phân trang)")
    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    public ResponseData<List<DressCategoryResponse>> getAllCategoriesList() {
        List<DressCategoryResponse> response = dressCategoryService.getAllCategories();
        return ResponseData.<List<DressCategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách danh mục thành công")
                .data(response)
                .build();
    }
}