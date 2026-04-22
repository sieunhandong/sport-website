package com.thanh_phoi_co.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_permission")
public class Permission {
    @Id
    private String name;
    private String description;

}
