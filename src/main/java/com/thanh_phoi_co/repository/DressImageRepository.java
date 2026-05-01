package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.model.DressImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DressImageRepository extends JpaRepository<DressImage, String> {

    List<DressImage> findByDressId(String dressId);

    Optional<DressImage> findByDressIdAndIsPrimaryTrue(String dressId);

    void deleteByDressId(String dressId);
}
