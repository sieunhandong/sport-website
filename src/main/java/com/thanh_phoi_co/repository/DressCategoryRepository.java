package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.model.DressCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DressCategoryRepository extends JpaRepository<DressCategory, String> {

    Optional<DressCategory> findByName(String name);

    boolean existsByName(String name);
}
