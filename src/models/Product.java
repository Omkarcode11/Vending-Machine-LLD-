package models;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int capacity;

    public Product(int id, String name, double price, int quantity, int capacity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void updateQuantity(int quantity) {
        if (!canUpdateQuantity(quantity)) {
            throw new IllegalArgumentException("Quantity is greater than capacity or less than 0");
        }
        this.quantity += quantity;
    }

    public boolean canUpdateQuantity(int quantity) {
        return this.quantity + quantity <= capacity && this.quantity + quantity >= 0;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + "]";
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void reduceQuantity(int quantity) {
        if (isAvailable()) {
            this.quantity -= quantity;
        }
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}