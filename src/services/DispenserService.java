package services;

import java.util.Map;

import models.Dispenser;
import models.Product;

public class DispenserService {
    Dispenser dispenser;
    public DispenserService(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    public void dispenseProduct(int id, int quantity) {
        if(!dispenser.isProductAvailable(id)) {
            throw new IllegalArgumentException("Product is not available");
        }

        if(quantity > dispenser.getProduct(id).getQuantity()) {
            throw new IllegalArgumentException("Product is not available");
        }

        dispenser.dispenseProduct(id, quantity);
    }

    public boolean canDispenseProducts(Map<Integer, Integer> products){
        for(Integer productId : products.keySet()){
            if(!dispenser.isProductAvailable(productId)) {
                return false;
            }

            if(products.get(productId) > dispenser.getProduct(productId).getQuantity()) {
                return false;
            }
        }
        return true;
    }

    public Product getProduct(int id) {
        return dispenser.getProduct(id);
    }

    public void addProduct(int id, String name, double price, int quantity, int capacity) {
        Product product = new Product(id, name, price, quantity, capacity);
        dispenser.addProduct(product);
    }

    public void addProduct(Product product) {
        dispenser.addProduct(product);
    }

    public void removeProduct(int id) {
    
        if(!dispenser.isProductAvailable(id)) {
            throw new IllegalArgumentException("Product is not available");
        }

        dispenser.removeProduct(id);
    }

    public void updateProductQuantity(int id, int quantity) {
        if(!dispenser.isProductAvailable(id)) {
            throw new IllegalArgumentException("Product is not available");
        }

        dispenser.getProduct(id).updateQuantity(quantity);
    }

    public void showProducts() {
        dispenser.showProducts();
    }

}
