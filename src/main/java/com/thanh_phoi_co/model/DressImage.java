package com.thanh_phoi_co.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_dress_image")
public class DressImage extends AbstractEntity<String> {

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @ManyToOne
    @JoinColumn(name = "dress_id", nullable = false)
    private Dress dress;
}
