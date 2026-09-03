package com.example.pos_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();
}