package states;

import java.util.List;
import java.util.Map;

import models.Product;
import services.*;

public class ProcessTransactionState implements VendingMachineState {

    DispenserService dispenserService;
    DenominationService denominationService;
    SelectProductService selectProductService;
    Map<Integer, Integer> money;

    public ProcessTransactionState(DispenserService dispenserService, DenominationService denominationService,
            SelectProductService selectProductService, Map<Integer, Integer> money) {
        this.dispenserService = dispenserService;
        this.denominationService = denominationService;
        this.selectProductService = selectProductService;
        this.money = money;
    }

    public void addProducts(List<Product> products) {
        throw new UnsupportedOperationException("Add products is not supported in process transaction state");
    }

    public void selectProduct(int id, int quantity) {
        throw new UnsupportedOperationException("Select product is not supported in process transaction state");
    }

    public void updateSelectedProduct(int id, int quantity) {
        throw new UnsupportedOperationException(
                "Update selected product is not supported in process transaction state");
    }

    public void removeSelectedProduct(int id) {
        throw new UnsupportedOperationException(
                "Remove selected product is not supported in process transaction state");
    }

    public void showSelectedProducts() {
        throw new UnsupportedOperationException("Show selected products is not supported in process transaction state");
    }

    public void clearSelectedProducts() {
        throw new UnsupportedOperationException(
                "Clear selected products is not supported in process transaction state");
    }

    public void insertMoney(Map<Integer, Integer> money) {
        throw new UnsupportedOperationException("Insert money is not supported in process transaction state");
    }

    public void giveChange(int amount) {
        throw new UnsupportedOperationException("Give change is not supported in process transaction state");
    }

    public void dispenseProducts() {
        throw new UnsupportedOperationException("Dispense products is not supported in process transaction state");
    }

    public void showProducts() {
        throw new UnsupportedOperationException("Show products is not supported in process transaction state");
    }

    public void processTransaction() {

        if (!dispenserService.canDispenseProducts(selectProductService.selectedProducts)) {
            throw new IllegalArgumentException("Products cannot be dispensed");
        }

        int selectedProductsPrice = 0;

        for (int productId : selectProductService.selectedProducts.keySet()) {
            selectedProductsPrice += selectProductService.selectedProducts.get(productId)
                    * dispenserService.getProduct(productId).getPrice();
        }

        int insertedAmountMoney = 0;

        for (int value : money.keySet()) {
            insertedAmountMoney += value * money.get(value);
        }

        int change = insertedAmountMoney - selectedProductsPrice;

        if (!denominationService.canAddMoney(money) || !denominationService.canWithdrawMoney(change)) {
            throw new IllegalArgumentException("Money is not enough or change cannot be given");
        }

        denominationService.addMoney(money);
        denominationService.withdrawMoney(change);

        for (int productId : selectProductService.selectedProducts.keySet()) {
            dispenserService.dispenseProduct(productId, selectProductService.selectedProducts.get(productId));
        }

        selectProductService.clear();

        System.out.println("Transaction completed successfully have a nice day");
    }

}
