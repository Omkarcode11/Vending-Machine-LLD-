package states;

import java.util.List;
import java.util.Map;

import models.Product;

public interface VendingMachineState {
    void addProducts(List<Product> products);

    void insertMoney(Map<Integer, Integer> money);

    void selectProduct(int id, int quantity);

    void updateSelectedProduct(int id, int quantity);

    void removeSelectedProduct(int id);

    void showSelectedProducts();

    void clearSelectedProducts();

    void showProducts();

    void processTransaction();

    void giveChange(int amount);

    void dispenseProducts();
}
