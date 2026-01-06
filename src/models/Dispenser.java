package models;

import java.util.HashMap;
import java.util.Map;

public class Dispenser {
    Map<Integer,Product> products;
    int id;
    
    public Dispenser(int id) {
        this.id = id;
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        this.products.put(product.getId(), product);
    }

    public void removeProduct(int id) {
        this.products.remove(id);
    }

    public Product getProduct(int id) {
        return this.products.get(id);
    }

    public boolean isProductAvailable(int id) {
        return this.products.get(id).isAvailable();
    }

    public boolean canDispenseProduct(int id, int quantity){
        Product product = this.getProduct(id);

        if(quantity < 0 || product == null || product.getQuantity() < quantity){
            return false;
        }

        return true;
    }

    public void dispenseProduct(int id, int quantity) {
        Product product = this.products.get(id);
        if(product == null || product.canUpdateQuantity(quantity)) {
            throw new IllegalArgumentException("Product is not available");
        }

        product.updateQuantity(quantity);
    }

    public void showProducts() {
        for(Product product : this.products.values()) {
            System.out.println(product.toString());
        }
    }
}
