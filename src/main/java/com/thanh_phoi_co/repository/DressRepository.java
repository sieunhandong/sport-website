package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.enums.DressStatus;
import com.thanh_phoi_co.model.Dress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DressRepository extends JpaRepository<Dress, String> {

    Optional<Dress> findByName(String name);

    Page<Dress> findByStatus(DressStatus status, Pageable pageable);

    Page<Dress> findByCategoryId(String categoryId, Pageable pageable);

    @Query("SELECT d FROM Dress d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Dress> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT d FROM Dress d
    WHERE d.status = :status
    AND (:categoryId IS NULL OR d.category.id = :categoryId)
    AND EXISTS (
        SELECT v FROM DressVariant v
        WHERE v.dress = d
        AND (:color IS NULL OR v.color = :color)
        AND (:size IS NULL OR v.size = :size)
    )
""")
    Page<Dress> filterDresses(
            @Param("status") DressStatus status,
            @Param("categoryId") String categoryId,
            @Param("color") String color,
            @Param("size") String size,
            Pageable pageable
    );

    @Query("SELECT d FROM Dress d JOIN d.variants v WHERE v.size = :size")
    Page<Dress> findByVariantSize(@Param("size") String size, Pageable pageable);

    @Query("SELECT d FROM Dress d JOIN d.variants v WHERE v.color = :color")
    Page<Dress> findByVariantColor(@Param("color") String color, Pageable pageable);

    @Query("SELECT DISTINCT v.size FROM DressVariant v")
    List<String> findAllSizes();

    @Query("SELECT DISTINCT v.color FROM DressVariant v")
    List<String> findAllColors();
}
