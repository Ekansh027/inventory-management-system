package com.casestudy.inventorymanagementsystem.services;

import com.casestudy.inventorymanagementsystem.dtos.InventoryResponseDto;

public interface InventoryService {
    InventoryResponseDto deductInventory(String productName, int productQuantity);

    InventoryResponseDto addInventory(String productName, int productQuantity);
}
