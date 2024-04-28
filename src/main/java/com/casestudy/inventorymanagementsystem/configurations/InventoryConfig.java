package com.casestudy.inventorymanagementsystem.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class InventoryConfig {

    @Value("${inventory.maxLimit}")
    private int maxInventoryLimit;
}
