package com.thanh_phoi_co.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thanh_phoi_co.dto.request.DressImageRequest;
import com.thanh_phoi_co.dto.response.DressImageResponse;
import com.thanh_phoi_co.exception.ResourceNotFoundException;
import com.thanh_phoi_co.model.Dress;
import com.thanh_phoi_co.model.DressImage;
import com.thanh_phoi_co.repository.DressImageRepository;
import com.thanh_phoi_co.repository.DressRepository;
import com.thanh_phoi_co.service.DressImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DressImageServiceImpl implements DressImageService {

    private final DressImageRepository dressImageRepository;
    private final DressRepository dressRepository;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public DressImageResponse updateImage(String imageId, DressImageRequest request) {
        DressImage image = dressImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        if (request.getIsPrimary() != null && request.getIsPrimary()) {
            // Đặt tất cả ảnh khác của váy này thành không primary
            List<DressImage> existingImages = dressImageRepository.findByDressId(image.getDress().getId());
            existingImages.forEach(img -> {
                if (!img.getId().equals(imageId)) {
                    img.setIsPrimary(false);
                }
            });
            dressImageRepository.saveAll(existingImages);
        }

        image.setImageUrl(request.getImageUrl());
        image.setAltText(request.getAltText());
        image.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : image.getIsPrimary());

        DressImage updatedImage = dressImageRepository.save(image);
        return mapToResponse(updatedImage);
    }

    @Override
    public void deleteImage(String imageId) {
        DressImage image = dressImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        dressImageRepository.delete(image);
    }

    @Override
    @Transactional(readOnly = true)
    public DressImageResponse getImageById(String imageId) {
        DressImage image = dressImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        return mapToResponse(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DressImageResponse> getImagesByDressId(String dressId) {
        List<DressImage> images = dressImageRepository.findByDressId(dressId);
        return images.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DressImageResponse> uploadImages(String dressId, MultipartFile[] files, List<String> altTexts, int primaryIndex) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Phải có ít nhất một file");
        }
        if (primaryIndex >= files.length || (primaryIndex < -1)) {
            throw new IllegalArgumentException("primaryIndex không hợp lệ");
        }

        Dress dress = dressRepository.findById(dressId)
                .orElseThrow(() -> new ResourceNotFoundException("Dress not found"));

        // Nếu có primary, reset tất cả ảnh hiện tại của váy thành không primary
        if (primaryIndex >= 0) {
            List<DressImage> existingImages = dressImageRepository.findByDressId(dressId);
            existingImages.forEach(img -> img.setIsPrimary(false));
            dressImageRepository.saveAll(existingImages);
        }

        List<DressImage> savedImages = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file.isEmpty()) continue;

            // Validation file
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("File " + (i+1) + " không phải ảnh");
            }
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File " + (i+1) + " quá lớn");
            }

            try {
                Map uploadResult = getCloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");

                // Lấy altText (mặc định "" nếu không có)
                String altText = (altTexts != null && i < altTexts.size()) ? altTexts.get(i) : "";

                // Set isPrimary
                boolean isPrimary = (i == primaryIndex);

                DressImage image = DressImage.builder()
                        .imageUrl(imageUrl)
                        .altText(altText)
                        .isPrimary(isPrimary)
                        .dress(dress)
                        .build();

                savedImages.add(dressImageRepository.save(image));

            } catch (IOException e) {
                log.error("Lỗi upload file " + (i+1), e);
                throw new RuntimeException("Lỗi upload file " + (i+1));
            }
        }

        return savedImages.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    private DressImageResponse mapToResponse(DressImage image) {
        return DressImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .altText(image.getAltText())
                .isPrimary(image.getIsPrimary())
                .build();
    }
}
