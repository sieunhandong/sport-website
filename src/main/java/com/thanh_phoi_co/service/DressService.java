package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.request.CreateDressRequest;
import com.thanh_phoi_co.dto.request.UpdateDressRequest;
import com.thanh_phoi_co.dto.response.DressResponse;
import com.thanh_phoi_co.enums.DressStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DressService {
    DressResponse createDress(CreateDressRequest request);
    DressResponse updateDress(String id, UpdateDressRequest request);
    void deleteDress(String id);
    DressResponse getDressById(String id);
    Page<DressResponse> getAllDresses(Pageable pageable);
    Page<DressResponse> searchDresses(String keyword, Pageable pageable);
    Page<DressResponse> filterDresses(DressStatus status, String categoryId, String color, String size, Pageable pageable);
    List<String> getAllColors();
    List<String> getAllSizes();
}
