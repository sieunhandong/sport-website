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
@Table(name = "tbl_dress_category")
public class DressCategory extends AbstractEntity<String> {

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonBackReference
    @Builder.Default
    private Set<Dress> dresses = new HashSet<>();
}
