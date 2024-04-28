package com.casestudy.inventorymanagementsystem.dtos;

import com.casestudy.inventorymanagementsystem.utilities.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {

    private String responseMessage;
    private String productName;
    private TransactionType transactionType;
    private int productQuantityAvailableInInventory;
}
