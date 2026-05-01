package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.DressImageRequest;
import com.thanh_phoi_co.dto.response.DressImageResponse;
import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.service.DressImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dress Image Controller")
public class DressImageController {

    private final DressImageService dressImageService;
    @Operation(summary = "Upload nhiều ảnh cho váy lên Cloudinary và lưu vào DB")
    @PostMapping(value = "/dress/{dressId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<List<DressImageResponse>> uploadImages(
            @PathVariable String dressId,
            @RequestPart("files") MultipartFile[] files,
            @RequestParam(value = "altTexts", required = false) List<String> altTexts,
            @RequestParam(value = "primaryIndex", defaultValue = "-1") int primaryIndex) {
        List<DressImageResponse> responses = dressImageService.uploadImages(dressId, files, altTexts, primaryIndex);
        return ResponseData.<List<DressImageResponse>>builder()
                .status(HttpStatus.CREATED.value())
                .message("Upload và lưu nhiều ảnh thành công")
                .data(responses)
                .build();
    }


    @Operation(summary = "Cập nhật ảnh")
    @PutMapping("/{imageId}")
    public ResponseData<DressImageResponse> updateImage(@PathVariable String imageId, @Valid @RequestBody DressImageRequest request) {
        DressImageResponse response = dressImageService.updateImage(imageId, request);
        return ResponseData.<DressImageResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật ảnh thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Xóa ảnh")
    @DeleteMapping("/{imageId}")
    public ResponseData<String> deleteImage(@PathVariable String imageId) {
        dressImageService.deleteImage(imageId);
        return ResponseData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Xóa ảnh thành công")
                .build();
    }

    @Operation(summary = "Lấy thông tin ảnh theo ID")
    @GetMapping("/{imageId}")
    public ResponseData<DressImageResponse> getImageById(@PathVariable String imageId) {
        DressImageResponse response = dressImageService.getImageById(imageId);
        return ResponseData.<DressImageResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin ảnh thành công")
                .data(response)
                .build();
    }

    @Operation(summary = "Lấy danh sách ảnh của váy")
    @GetMapping("/dress/{dressId}")
    public ResponseData<List<DressImageResponse>> getImagesByDressId(@PathVariable String dressId) {
        List<DressImageResponse> response = dressImageService.getImagesByDressId(dressId);
        return ResponseData.<List<DressImageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách ảnh thành công")
                .data(response)
                .build();
    }
}
