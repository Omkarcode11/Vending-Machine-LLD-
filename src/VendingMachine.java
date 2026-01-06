import java.util.List;
import java.util.Map;

import models.*;
import services.*;
import states.IdleState;
import states.InsertMoneyState;
import states.ProcessTransactionState;
import states.VendingMachineState;

public class VendingMachine {
    int id;
    DispenserService dispenserService;
    DenominationService denominationService;
    SelectProductService selectProductService;
    VendingMachineState state;
    Map<Integer, Integer> money;

    public VendingMachine(int id, DispenserService dispenserService, DenominationService denominationService,
            SelectProductService selectProductService, TransactionService transactionService) {
        this.id = id;
        this.dispenserService = dispenserService;
        this.denominationService = denominationService;
        this.selectProductService = selectProductService;
    }

    public void addProducts(List<Product> products) {
        state.addProducts(products);
    }

    public void selectProduct(int id, int quantity) {
        state.selectProduct(id, quantity);
        setState(new InsertMoneyState(money));
        ;
    }

    public void setState(VendingMachineState state) {
        this.state = state;
    }

    public void updateSelectedProduct(int id, int quantity) {
        this.state.selectProduct(id, quantity);
    }

    public void removeSelectedProduct(int id) {
        state.removeSelectedProduct(id);
    }

    public void showSelectedProducts() {
        state.showSelectedProducts();
    }

    public void clearSelectedProducts() {
        state.clearSelectedProducts();
        setState(new IdleState(selectProductService, dispenserService));
    }

    public void insertMoney(Map<Integer, Integer> money) {
        this.money = money;
        setState(new ProcessTransactionState(dispenserService, denominationService, selectProductService, money));
    }

    public void giveChange(int amount) {
        state.giveChange(amount);
    }

    public void dispenseProducts() {
        state.dispenseProducts();
    }

    public void showProducts() {
        dispenserService.showProducts();
    }

    public void processTransaction() {
        state.processTransaction();

        this.state = new IdleState(selectProductService, dispenserService);
    }

}
