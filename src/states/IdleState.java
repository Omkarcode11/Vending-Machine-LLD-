package states;

import java.util.List;
import java.util.Map;

import models.Product;
import services.DispenserService;
import services.SelectProductService;

public class IdleState implements VendingMachineState {

    SelectProductService selectProductService;
    DispenserService dispenserService;

    public IdleState(SelectProductService selectProductService, DispenserService dispenserService) {
        this.selectProductService = selectProductService;
        this.dispenserService = dispenserService;
    }

    public void addProduct() {
        throw new UnsupportedOperationException("Add product is not supported in idle state");
    }

    public void addProducts(List<Product> products) {
        throw new UnsupportedOperationException("Add products is not supported in idle state");
    }

    public void selectProduct(int id, int quantity) {
        selectProductService.selectProduct(id, quantity);
    }

    public void updateSelectedProduct(int id, int quantity) {
        selectProductService.update(id, quantity);
    }

    public void removeSelectedProduct(int id) {
        selectProductService.removeProduct(id);
    }

    public void showSelectedProducts() {
        selectProductService.showSelectedProducts();
    }

    public void clearSelectedProducts() {
        selectProductService.clear();
    }

    public void insertMoney(Map<Integer, Integer> money) {
        throw new UnsupportedOperationException("Insert money is not supported in idle state");
    }

    public void giveChange(int amount) {
        throw new UnsupportedOperationException("Give change is not supported in idle state");
    }

    public void dispenseProducts() {
        throw new UnsupportedOperationException("Dispense products is not supported in idle state");
    }

    public void showProducts() {
        dispenserService.showProducts();
    }

    public void processTransaction() {
        throw new UnsupportedOperationException("Process transaction is not supported in idle state");
    }
}
