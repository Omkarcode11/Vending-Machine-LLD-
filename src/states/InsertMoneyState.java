package states;

import java.util.List;
import java.util.Map;

import models.Product;

public class InsertMoneyState implements VendingMachineState {

    Map<Integer, Integer> money;

    public InsertMoneyState(Map<Integer, Integer> money) {
        this.money = money;
    }

    public void addProducts(List<Product> products) {
        throw new UnsupportedOperationException("Add products is not supported in insert money state");
    }

    public void selectProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Select product is not supported in insert money state");
    }

    public void updateSelectedProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Update selected product is not supported in insert money state");
    }

    public void removeSelectedProduct(int id) {
        throw new UnsupportedOperationException("Remove selected product is not supported in insert money state");
    }

    public void showSelectedProducts() {
        throw new UnsupportedOperationException("Show selected products is not supported in insert money state");
    }

    public void clearSelectedProducts() {
        throw new UnsupportedOperationException("Clear selected products is not supported in insert money state");
    }

    public void insertMoney(Map<Integer, Integer> money) {
        this.money = money;
    }

    public void giveChange(int amount) {
        throw new UnsupportedOperationException("Give change is not supported in insert money state");
    }

    public void dispenseProducts() {
        throw new UnsupportedOperationException("Dispense products is not supported in insert money state");
    }

    public void showProducts() {
        throw new UnsupportedOperationException("Show products is not supported in insert money state");
    }

    public void processTransaction() {
        throw new UnsupportedOperationException("Process transaction is not supported in insert money state");
    }

}
