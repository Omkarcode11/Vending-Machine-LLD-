package models;

import java.util.List;
import java.util.UUID;

import enums.TransactionStatus;
import enums.TransactionType;

public class Transaction {
    int id;
    double amount;
    TransactionType type;
    TransactionStatus status;
    List<Integer> products;
    int totalPrice;

    public Transaction(double amount, TransactionType type, List<Integer> products,
            int totalPrice) {
        this.id = UUID.randomUUID().hashCode();
        this.amount = amount;
        this.type = type;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public Transaction() {
        this.id = UUID.randomUUID().hashCode();
        this.status = TransactionStatus.CREATED;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public List<Integer> getProducts() {
        return products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void addProducts(List<Integer> products) {
        this.products.addAll(products);
    }

    public void calculateTotalPrice(int totalPrice) {
        this.totalPrice += totalPrice;
    }
}
