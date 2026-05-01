package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.model.DressVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DressVariantRepository extends JpaRepository<DressVariant, String> {

    List<DressVariant> findByDressId(String dressId);

    Optional<DressVariant> findByDressIdAndSizeAndColor(String dressId, String size, String color);

    @Query("SELECT DISTINCT dv.size FROM DressVariant dv WHERE dv.dress.id = :dressId")
    List<String> findSizesByDressId(@Param("dressId") String dressId);

    @Query("SELECT DISTINCT dv.color FROM DressVariant dv WHERE dv.dress.id = :dressId")
    List<String> findColorsByDressId(@Param("dressId") String dressId);

    @Query("SELECT dv FROM DressVariant dv WHERE dv.dress.id = :dressId AND dv.quantityAvailable > 0")
    List<DressVariant> findAvailableVariantsByDressId(@Param("dressId") String dressId);

    void deleteByDressId(String dressId);
}