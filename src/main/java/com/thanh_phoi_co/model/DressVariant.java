package com.thanh_phoi_co.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_dress_variant")
public class DressVariant extends AbstractEntity<String> {

    @Column(name = "dress_size", nullable = false, length = 20)
    private String size; // XS, S, M, L, XL, XXL

    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable; // Số lượng có sẵn cho biến thể này

    @Column(name = "quantity_rented")
    private Integer quantityRented = 0; // Số lượng đã cho thuê cho biến thể này

    @ManyToOne
    @JoinColumn(name = "dress_id", nullable = false)
    @JsonBackReference
    private Dress dress;

    @OneToMany(mappedBy = "variant")
    @JsonBackReference
    private Set<Rental> rentals = new HashSet<>();
}
