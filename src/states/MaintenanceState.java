package states;

import java.util.List;
import java.util.Map;

import models.Product;
import services.DenominationService;
import services.DispenserService;

public class MaintenanceState implements VendingMachineState {

    DispenserService dispenserService;
    DenominationService denominationService;

    public MaintenanceState(DispenserService dispenserService, DenominationService denominationService) {
        this.dispenserService = dispenserService;
        this.denominationService = denominationService;
    }

    public void addProducts(List<Product> products) {
        for (Product product : products) {
            dispenserService.addProduct(product);
        }
    }

    public void selectProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Select product is not supported in maintenance state");
    }

    public void updateSelectedProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Update selected product is not supported in maintenance state");
    }

    public void removeSelectedProduct(int id) {
        throw new UnsupportedOperationException("Remove selected product is not supported in maintenance state");
    }

    public void showSelectedProducts() {
        throw new UnsupportedOperationException("Show selected products is not supported in maintenance state");
    }

    public void clearSelectedProducts() {
        throw new UnsupportedOperationException("Clear selected products is not supported in maintenance state");
    }

    public void insertMoney(Map<Integer, Integer> money) {
        throw new UnsupportedOperationException("Insert money is not supported in maintenance state");
    }

    public void giveChange(int amount) {
        throw new UnsupportedOperationException("Give change is not supported in maintenance state");
    }

    public void dispenseProducts() {
        throw new UnsupportedOperationException("Dispense products is not supported in maintenance state");
    }

    public void showProducts() {
        dispenserService.showProducts();
    }

    public void processTransaction() {

        throw new UnsupportedOperationException("Process transaction is not supported in maintenance state");
    }

}
