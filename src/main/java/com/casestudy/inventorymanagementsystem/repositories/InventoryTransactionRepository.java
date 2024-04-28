package com.casestudy.inventorymanagementsystem.repositories;

import com.casestudy.inventorymanagementsystem.models.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
}
