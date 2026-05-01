package com.thanh_phoi_co.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thanh_phoi_co.enums.DressStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_dress")
public class Dress extends AbstractEntity<String> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "CLOB")
    private String description;

    @Column(name = "material", length = 100)
    private String material; // Chất liệu: lụa, voan, cotton...

    @Column(name = "rental_price", nullable = false)
    private BigDecimal rentalPrice; // Giá thuê/ngày

    @Column(name = "deposit_amount", nullable = false)
    private BigDecimal depositAmount; // Tiền cọc

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DressStatus status = DressStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private DressCategory category;

    @OneToMany(mappedBy = "dress", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Builder.Default
    private Set<DressImage> images = new HashSet<>();

    @OneToMany(mappedBy = "dress", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<DressVariant> variants = new HashSet<>();

    @OneToMany(mappedBy = "dress")
    @JsonBackReference
    private Set<Rental> rentals = new HashSet<>();
}
