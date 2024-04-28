package com.casestudy.inventorymanagementsystem.controllers;

import com.casestudy.inventorymanagementsystem.dtos.InventoryRequestDto;
import com.casestudy.inventorymanagementsystem.dtos.InventoryResponseDto;
import com.casestudy.inventorymanagementsystem.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("v1/deduct")
    public ResponseEntity<InventoryResponseDto> deductInventory(@RequestBody InventoryRequestDto inventoryDeductRequest){
        return ResponseEntity.ok(inventoryService.deductInventory(inventoryDeductRequest.getProductName(), inventoryDeductRequest.getQuantity()));
    }

    @PostMapping("v1/add")
    public ResponseEntity<InventoryResponseDto> addInventory(@RequestBody InventoryRequestDto inventoryAddRequest){
        return ResponseEntity.ok(inventoryService.addInventory(inventoryAddRequest.getProductName(), inventoryAddRequest.getQuantity()));
    }


}
