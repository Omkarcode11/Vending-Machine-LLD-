package states;

import java.util.List;
import java.util.Map;

import models.Product;

public class OutOfServiceState implements VendingMachineState {
    public void addProducts(List<Product> products) {
        throw new UnsupportedOperationException("Add products is not supported in out of service state");
    }

    public void selectProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Select product is not supported in out of service state");
    }

    public void updateSelectedProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Update selected product is not supported in out of service state");
    }

    public void removeSelectedProduct(int id) {
        throw new UnsupportedOperationException("Remove selected product is not supported in out of service state");
    }

    public void showSelectedProducts() {
        throw new UnsupportedOperationException("Show selected products is not supported in out of service state");
    }

    public void clearSelectedProducts() {
        throw new UnsupportedOperationException("Clear selected products is not supported in out of service state");
    }

    public void insertMoney(Map<Integer, Integer> money) {
        throw new UnsupportedOperationException("Insert money is not supported in out of service state");
    }

    public void giveChange(int amount) {
        throw new UnsupportedOperationException("Give change is not supported in out of service state");
    }

    public void dispenseProducts() {
        throw new UnsupportedOperationException("Dispense products is not supported in out of service state");
    }

    public void showProducts() {
        throw new UnsupportedOperationException("Show products is not supported in out of service state");
    }

    public void processTransaction() {
        throw new UnsupportedOperationException("Process transaction is not supported in out of service state");

    }

}
