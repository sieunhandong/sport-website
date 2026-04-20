package com.thanh_phoi_co.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private T id;

    @Column(name = "created_at")
    @CreationTimestamp
    private String createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private String updated_at;
}
