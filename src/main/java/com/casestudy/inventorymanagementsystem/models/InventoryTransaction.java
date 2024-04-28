package com.casestudy.inventorymanagementsystem.models;

import com.casestudy.inventorymanagementsystem.utilities.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name ="INVENTORY_TRANSACTION")
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "product_quantity_processed")
    private int productQuantityProcessed;

    private Date timestamp;
}
