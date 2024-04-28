package com.casestudy.inventorymanagementsystem.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int quantity;

    @Column(name = "max_product_level_quantity")
    private int maxProductQuantity;
}
