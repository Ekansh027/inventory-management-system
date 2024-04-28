package com.casestudy.inventorymanagementsystem.services.impl;

import com.casestudy.inventorymanagementsystem.configurations.InventoryConfig;
import com.casestudy.inventorymanagementsystem.dtos.InventoryResponseDto;
import com.casestudy.inventorymanagementsystem.models.InventoryTransaction;
import com.casestudy.inventorymanagementsystem.models.Product;
import com.casestudy.inventorymanagementsystem.repositories.InventoryTransactionRepository;
import com.casestudy.inventorymanagementsystem.repositories.ProductRepository;
import com.casestudy.inventorymanagementsystem.services.InventoryService;
import com.casestudy.inventorymanagementsystem.utilities.InventoryConstants;
import com.casestudy.inventorymanagementsystem.utilities.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Autowired
    private InventoryConfig inventoryConfig;

    /**
     * Deducts the specified quantity of a product from the inventory.
     *
     * @param productName The name of the product to deduct from inventory.
     * @param productQuantityToDeduct The quantity of the product to deduct.
     * @return An InventoryResponseDto object indicating the result of the deduction operation.
     */
    @Override
    public InventoryResponseDto deductInventory(String productName, int productQuantityToDeduct) {
        Product product = getProduct(productName);
        int maxProductLimitInInventory = getMaxLimitProductLevelInventory(product);
        int updatedProductQuantityInInventory;
        synchronized (product){
            if(productQuantityToDeduct > getMaxLimitGlobalInventory()){
                throw new IllegalArgumentException("Requested products to be deducted are more than the max limit of inventory");
            }
            if(productQuantityToDeduct > maxProductLimitInInventory){
                throw new IllegalArgumentException("Requested products to be deducted are more than max Limit of product: " + product.getName());
            }
            if(productQuantityToDeduct > product.getQuantity()){
                throw new IllegalArgumentException("Requested products to be deducted are more than available products");
            }
            updatedProductQuantityInInventory = updateProductQuantity(product, -productQuantityToDeduct, maxProductLimitInInventory);
            createInventoryTransaction(product, productQuantityToDeduct, TransactionType.INVENTORY_DEDUCTION);
        }
        return new InventoryResponseDto(InventoryConstants.SUCCESS, product.getName(), TransactionType.INVENTORY_DEDUCTION, updatedProductQuantityInInventory);
    }

    /**
     * Add the specified quantity of a product in the inventory.
     *
     * @param productName The name of the product to deduct from inventory.
     * @param productQuantityToAdd The quantity of the product to add.
     * @return An InventoryResponseDto object indicating the result of the addition operation.
     */
    @Override
    public InventoryResponseDto addInventory(String productName, int productQuantityToAdd) {
        if(productQuantityToAdd < 0){
            throw new IllegalArgumentException("Product to be added should be atleast greater than 0");
        }
        Product product = getProduct(productName);
        int maxProductLimitInInventory = getMaxLimitProductLevelInventory(product);
        int updatedProductQuantityInInventory;
        synchronized (product){
            if(productQuantityToAdd > getMaxLimitGlobalInventory()){
                throw new IllegalArgumentException("Requested products to be added are more than the max limit of inventory");
            }
            updatedProductQuantityInInventory = updateProductQuantity(product, productQuantityToAdd, maxProductLimitInInventory);
            createInventoryTransaction(product, productQuantityToAdd, TransactionType.INVENTORY_ADDITION);
        }
        return new InventoryResponseDto(InventoryConstants.SUCCESS, product.getName(), TransactionType.INVENTORY_ADDITION, updatedProductQuantityInInventory);
    }

    /**
     * Retrieves a product from the database by its name.
     *
     * @param productName The name of the product to retrieve.
     * @return The Product object with the specified name.
     */
    private Product getProduct(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(()-> new IllegalArgumentException("Product not found"));
    }

    /**
     * Updates the quantity of a product in the inventory and saves the changes to the database.
     *
     * @param product The Product object to update.
     * @param quantityToUpdate The quantity to add (positive) or deduct (negative).
     * @param maxProductLimitInInventory The maximum limit of the product quantity in the inventory.
     * @return The updated quantity of the product in the inventory.
     */
    private int updateProductQuantity(Product product, int quantityToUpdate, int maxProductLimitInInventory) {
        int updatedProductQuantity = product.getQuantity() + quantityToUpdate;
        if(updatedProductQuantity < 0 || updatedProductQuantity > maxProductLimitInInventory){
            throw new IllegalArgumentException("Invalid product quantity in inventory");
        }
        product.setQuantity(updatedProductQuantity);
        productRepository.save(product);
        return updatedProductQuantity;
    }

    /**
     * Retrieves the maximum limit of the global inventory.
     *
     * @return The maximum limit of the global inventory.
     */
    private int getMaxLimitGlobalInventory(){
        return inventoryConfig.getMaxInventoryLimit();
    }

    /**
     * Retrieves the maximum limit of the product quantity in the inventory.
     *
     * @param product The Product object for which to retrieve the maximum limit.
     * @return The maximum limit of the product quantity in the inventory.
     */
    private int getMaxLimitProductLevelInventory(Product product){
        return product.getMaxProductQuantity();
    }

    /**
     * Creates an inventory transaction record for the given product.
     *
     * @param product The Product object associated with the transaction.
     * @param productQuantity The quantity of the product involved in the transaction.
     * @param transactionType The type of transaction (e.g., addition or deduction).
     */
    private void createInventoryTransaction(Product product, int productQuantity, TransactionType transactionType) {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.setProductId(product.getId());
        inventoryTransaction.setType(transactionType);
        inventoryTransaction.setProductQuantityProcessed(productQuantity);
        inventoryTransaction.setTimestamp(new Date());
        inventoryTransactionRepository.save(inventoryTransaction);
    }
}
