package com.thanh_phoi_co.service.impl;

import com.thanh_phoi_co.dto.request.CreateDressRequest;
import com.thanh_phoi_co.dto.request.UpdateDressRequest;
import com.thanh_phoi_co.dto.response.DressImageResponse;
import com.thanh_phoi_co.dto.response.DressResponse;
import com.thanh_phoi_co.dto.response.DressVariantResponse;
import com.thanh_phoi_co.enums.DressStatus;
import com.thanh_phoi_co.exception.ResourceNotFoundException;
import com.thanh_phoi_co.model.Dress;
import com.thanh_phoi_co.model.DressCategory;
import com.thanh_phoi_co.model.DressVariant;
import com.thanh_phoi_co.repository.DressCategoryRepository;
import com.thanh_phoi_co.repository.DressRepository;
import com.thanh_phoi_co.repository.DressVariantRepository;
import com.thanh_phoi_co.repository.ReviewRepository;
import com.thanh_phoi_co.service.DressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DressServiceImpl implements DressService {

    private final DressRepository dressRepository;
    private final DressCategoryRepository dressCategoryRepository;
    private final DressVariantRepository dressVariantRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public DressResponse createDress(CreateDressRequest request) {
        DressCategory category = dressCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy tiêu đề"));

        Dress dress = Dress.builder()
                .name(request.getName())
                .description(request.getDescription())
                .material(request.getMaterial())
                .rentalPrice(request.getRentalPrice())
                .depositAmount(request.getDepositAmount())
                .status(DressStatus.AVAILABLE)
                .category(category)
                .build();

        Dress savedDress = dressRepository.save(dress);

        // Tạo variants
        List<DressVariant> variants = request.getVariants().stream()
                .map(variantRequest -> DressVariant.builder()
                        .size(variantRequest.getSize())
                        .color(variantRequest.getColor())
                        .quantityAvailable(variantRequest.getQuantityAvailable())
                        .quantityRented(0)
                        .dress(savedDress)
                        .build())
                .collect(Collectors.toList());

        dressVariantRepository.saveAll(variants);
        savedDress.setVariants(new HashSet<>(variants));

        return mapToResponse(savedDress);
    }


    @Override
    public DressResponse updateDress(String id, UpdateDressRequest request) {
        Dress dress = dressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy váy"));

        if (request.getName() != null) dress.setName(request.getName());
        if (request.getDescription() != null) dress.setDescription(request.getDescription());
        if (request.getMaterial() != null) dress.setMaterial(request.getMaterial());
        if (request.getRentalPrice() != null) dress.setRentalPrice(request.getRentalPrice());
        if (request.getDepositAmount() != null) dress.setDepositAmount(request.getDepositAmount());

        // Cập nhật variants nếu có
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            // Xóa variants cũ
            dressVariantRepository.deleteByDressId(id);

            // Tạo variants mới
            List<DressVariant> newVariants = request.getVariants().stream()
                    .map(variantRequest -> DressVariant.builder()
                            .size(variantRequest.getSize())
                            .color(variantRequest.getColor())
                            .quantityAvailable(variantRequest.getQuantityAvailable())
                            .quantityRented(0)
                            .dress(dress)
                            .build())
                    .collect(Collectors.toList());

            dressVariantRepository.saveAll(newVariants);
            dress.setVariants(new HashSet<>(newVariants));
        }

        Dress updatedDress = dressRepository.save(dress);
        return mapToResponse(updatedDress);
    }


    @Override
    public void deleteDress(String id) {
        Dress dress = dressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dress not found"));
        dressRepository.delete(dress);
    }

    @Override
    @Transactional(readOnly = true)
    public DressResponse getDressById(String id) {
        Dress dress = dressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dress not found"));
        return mapToResponse(dress);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DressResponse> getAllDresses(Pageable pageable) {
        return dressRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DressResponse> searchDresses(String keyword, Pageable pageable) {
        return dressRepository.searchByKeyword(keyword, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DressResponse> filterDresses(DressStatus status, String categoryId, String color, String size, Pageable pageable) {
        return dressRepository.filterDresses(status, categoryId, color, size, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllColors() {
        return dressRepository.findAllColors();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllSizes() {
        return dressRepository.findAllSizes();
    }

    private DressResponse mapToResponse(Dress dress) {
        Double averageRating = reviewRepository.findAverageRatingByDressId(dress.getId());
        Long reviewCount = reviewRepository.findReviewCountByDressId(dress.getId());

        List<DressVariantResponse> variantResponses = dress.getVariants().stream()
                .map(variant -> DressVariantResponse.builder()
                        .id(variant.getId())
                        .size(variant.getSize())
                        .color(variant.getColor())
                        .quantityAvailable(variant.getQuantityAvailable())
                        .quantityRented(variant.getQuantityRented())
                        .build())
                .collect(Collectors.toList());

        return DressResponse.builder()
                .id(dress.getId())
                .name(dress.getName())
                .description(dress.getDescription())
                .material(dress.getMaterial())
                .rentalPrice(dress.getRentalPrice())
                .depositAmount(dress.getDepositAmount())
                .status(dress.getStatus())
                .categoryId(dress.getCategory().getId())
                .categoryName(dress.getCategory().getName())
                .images(dress.getImages().stream()
                        .map(img -> DressImageResponse.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .isPrimary(img.getIsPrimary())
                                .build())
                        .collect(Collectors.toList()))
                .variants(variantResponses)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount.intValue() : 0)
                .build();
    }
}
