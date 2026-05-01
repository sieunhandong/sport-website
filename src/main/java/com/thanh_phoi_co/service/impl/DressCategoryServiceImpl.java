package com.thanh_phoi_co.service.impl;

import com.thanh_phoi_co.dto.request.DressCategoryRequest;
import com.thanh_phoi_co.dto.response.DressCategoryResponse;
import com.thanh_phoi_co.exception.AppException;
import com.thanh_phoi_co.exception.ErrorCode;
import com.thanh_phoi_co.exception.ResourceNotFoundException;
import com.thanh_phoi_co.model.DressCategory;
import com.thanh_phoi_co.repository.DressCategoryRepository;
import com.thanh_phoi_co.service.DressCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DressCategoryServiceImpl implements DressCategoryService {

    private final DressCategoryRepository dressCategoryRepository;

    @Override
    public DressCategoryResponse createCategory(DressCategoryRequest request) {
        if (dressCategoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        DressCategory category = DressCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        DressCategory savedCategory = dressCategoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Override
    public DressCategoryResponse updateCategory(String id, DressCategoryRequest request) {
        DressCategory category = dressCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu đề"));

        if (!category.getName().equals(request.getName()) && dressCategoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());

        DressCategory updatedCategory = dressCategoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(String id) {
        DressCategory category = dressCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu đề"));
        dressCategoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public DressCategoryResponse getCategoryById(String id) {
        DressCategory category = dressCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu đề"));
        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DressCategoryResponse> getAllCategories(Pageable pageable) {
        return dressCategoryRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DressCategoryResponse> getAllCategories() {
        return dressCategoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DressCategoryResponse mapToResponse(DressCategory category) {
        return DressCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .dressCount((long) category.getDresses().size())
                .build();
    }
}
