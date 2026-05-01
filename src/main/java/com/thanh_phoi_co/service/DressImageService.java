package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.request.DressImageRequest;
import com.thanh_phoi_co.dto.response.DressImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DressImageService {
    DressImageResponse updateImage(String imageId, DressImageRequest request);
    void deleteImage(String imageId);
    DressImageResponse getImageById(String imageId);
    List<DressImageResponse> getImagesByDressId(String dressId);
    List<DressImageResponse> uploadImages(String dressId, MultipartFile[] files, List<String> altTexts, int primaryIndex);
}
