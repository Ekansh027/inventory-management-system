package com.casestudy.inventorymanagementsystem.dtos;

import lombok.Data;

@Data
public class InventoryRequestDto {

    private String productName;
    private int quantity;
}
