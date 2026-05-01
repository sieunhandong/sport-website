package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.request.DressCategoryRequest;
import com.thanh_phoi_co.dto.response.DressCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DressCategoryService {
    DressCategoryResponse createCategory(DressCategoryRequest request);
    DressCategoryResponse updateCategory(String id, DressCategoryRequest request);
    void deleteCategory(String id);
    DressCategoryResponse getCategoryById(String id);
    Page<DressCategoryResponse> getAllCategories(Pageable pageable);
    List<DressCategoryResponse> getAllCategories();
}
