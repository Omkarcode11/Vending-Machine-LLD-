package services;

import java.util.*;

import models.Dispenser;

public class SelectProductService {
    public Map<Integer, Integer> selectedProducts; // product id and quantity
    public Dispenser dispenser;

    public SelectProductService(Dispenser dispenser) {
        this.selectedProducts = new HashMap<>();
        this.dispenser = dispenser;
    }

    public void selectProduct(int productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (!dispenser.canDispenseProduct(productId, quantity)) {
            throw new IllegalArgumentException("Product is not available");
        }

        int currentQuantity = this.selectedProducts.getOrDefault(productId, 0);
        this.selectedProducts.put(productId, currentQuantity + quantity);
    }

    public void clear() {
        this.selectedProducts.clear();
    }

    public void update(int productId, int quantity) {
        if (selectedProducts.get(productId) == null) {
            System.out.print("Product is not available");
        }
        int newQuantity = selectedProducts.get(productId) + quantity;
        if (!dispenser.canDispenseProduct(productId, newQuantity)) {
            System.out.println("Product is not available");
        }

        this.selectedProducts.put(productId, newQuantity);
    }

    public void removeProduct(int productId) {
        if (!dispenser.isProductAvailable(productId)) {
            System.out.println("Product is not available");
        }

        this.selectedProducts.remove(productId);
    }

    public void showSelectedProducts() {
        for (Map.Entry<Integer, Integer> entry : selectedProducts.entrySet()) {
            System.out.println("Product ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
    }

}
