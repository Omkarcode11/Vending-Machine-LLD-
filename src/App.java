import java.util.*;
import models.*;
import services.*;
import states.*;

public class App {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   VENDING MACHINE ROBUSTNESS TEST SUITE  ");
        System.out.println("==========================================\n");

        testCase1_SuccessfulPurchase();
        testCase2_InsufficientMoney();
        testCase3_OutOfStock();
        testCase4_NoChangeAvailable();
        testCase5_MultipleItemsPurchase();
        testCase6_CancelAndClear();
        testCase7_InvalidStateTransitions();
        testCase8_MaintenanceMode();
        testCase9_EdgeCases();

        System.out.println("\n==========================================");
        System.out.println("           TESTS COMPLETED                ");
        System.out.println("==========================================");
    }

    private static VendingMachine setupVendingMachine() {
        // Setup Denominations
        Denomination d10 = new Denomination(1, 10, 100);
        d10.addMoney(50); // Add 50 coins of 10
        DenominationChainService chain10 = new DenominationChainService(d10);

        Denomination d5 = new Denomination(2, 5, 100);
        d5.addMoney(100); // Add 100 coins of 5
        DenominationChainService chain5 = new DenominationChainService(d5);

        Denomination d1 = new Denomination(3, 1, 500);
        d1.addMoney(200); // Add 200 coins of 1
        DenominationChainService chain1 = new DenominationChainService(d1);

        chain10.setNext(chain5);
        chain5.setNext(chain1);

        DenominationService denominationService = new DenominationService(chain10);

        // Setup Dispenser
        Dispenser dispenser = new Dispenser(1);
        DispenserService dispenserService = new DispenserService(dispenser);

        // Add some products
        // Note: Product capacity is 0 by default and no setter, so updateQuantity might
        // fail in current code
        Product p1 = new Product(101, "Coke", 15.0, 10);
        Product p2 = new Product(102, "Pepsi", 12.0, 5);
        Product p3 = new Product(103, "Water", 10.0, 20);

        dispenser.addProduct(p1);
        dispenser.addProduct(p2);
        dispenser.addProduct(p3);

        SelectProductService selectProductService = new SelectProductService(dispenser);
        TransactionService transactionService = new TransactionService();

        VendingMachine vm = new VendingMachine(1, dispenserService, denominationService, selectProductService,
                transactionService);
        vm.setState(new IdleState(selectProductService, dispenserService));

        return vm;
    }

    private static void testCase1_SuccessfulPurchase() {
        System.out.println("--- Test Case 1: Successful Purchase ---");
        try {
            VendingMachine vm = setupVendingMachine();

            System.out.println("Selecting Coke (ID: 101, Qty: 1)...");
            vm.selectProduct(101, 1);

            Map<Integer, Integer> moneyInserted = new HashMap<>();
            moneyInserted.put(10, 2); // 20 units
            System.out.println("Inserting 20 units (2x10)...");
            vm.insertMoney(moneyInserted);

            System.out.println("Processing transaction...");
            vm.processTransaction();
            System.out.println("Observed Result: Success");
        } catch (Exception e) {
            System.out.println("Observed Result (Caught exception): " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase2_InsufficientMoney() {
        System.out.println("--- Test Case 2: Insufficient Money ---");
        try {
            VendingMachine vm = setupVendingMachine();

            System.out.println("Selecting Water (Price: 10, Qty: 1)...");
            vm.selectProduct(103, 1);

            Map<Integer, Integer> moneyInserted = new HashMap<>();
            moneyInserted.put(5, 1); // 5 units
            System.out.println("Inserting 5 units...");
            vm.insertMoney(moneyInserted);

            vm.processTransaction();
        } catch (Exception e) {
            System.out.println("Observed Result (Caught exception): " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase3_OutOfStock() {
        System.out.println("--- Test Case 3: Out of Stock ---");
        try {
            VendingMachine vm = setupVendingMachine();

            System.out.print("Selecting Pepsi (5 available, requesting 10): ");
            vm.selectProduct(102, 10);

            Map<Integer, Integer> moneyInserted = new HashMap<>();
            moneyInserted.put(10, 15);
            vm.insertMoney(moneyInserted);

            vm.processTransaction();
        } catch (Exception e) {
            System.out.println("Observed Result (Caught exception): " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase4_NoChangeAvailable() {
        System.out.println("--- Test Case 4: No Change Available ---");
        try {
            Dispenser dispenser = new Dispenser(1);
            DispenserService dispenserService = new DispenserService(dispenser);
            Product p1 = new Product(101, "Coke", 15.0, 10);
            dispenser.addProduct(p1);

            // Empty chain
            Denomination d50 = new Denomination(1, 50, 1);
            DenominationChainService chain50 = new DenominationChainService(d50);
            DenominationService denominationService = new DenominationService(chain50);

            SelectProductService selectProductService = new SelectProductService(dispenser);
            TransactionService transactionService = new TransactionService();

            VendingMachine vm = new VendingMachine(1, dispenserService, denominationService, selectProductService,
                    transactionService);
            vm.setState(new IdleState(selectProductService, dispenserService));

            System.out.println("Selecting Coke (15.0)...");
            vm.selectProduct(101, 1);

            Map<Integer, Integer> moneyInserted = new HashMap<>();
            moneyInserted.put(50, 1);
            System.out.println("Inserting 50, no change available...");
            vm.insertMoney(moneyInserted);

            vm.processTransaction();
        } catch (Exception e) {
            System.out.println("Observed Result (Caught exception): " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase5_MultipleItemsPurchase() {
        System.out.println("--- Test Case 5: Multiple Items Purchase ---");
        try {
            VendingMachine vm = setupVendingMachine();
            System.out.println("Attempting to select multiple items...");
            vm.selectProduct(101, 1);
            System.out.println("Updating selection (attempting to add another product)...");
            vm.updateSelectedProduct(102, 1);

            Map<Integer, Integer> moneyInserted = new HashMap<>();
            moneyInserted.put(10, 5);
            vm.insertMoney(moneyInserted);

            vm.processTransaction();
        } catch (Exception e) {
            System.out.println("Observed Result: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase6_CancelAndClear() {
        System.out.println("--- Test Case 6: Cancel and Clear ---");
        try {
            VendingMachine vm = setupVendingMachine();
            System.out.println("Selecting Coke...");
            vm.selectProduct(101, 1);
            System.out.println("Clearing selection...");
            vm.clearSelectedProducts();
            System.out.println("Success: Selection cleared.");
        } catch (Exception e) {
            System.out.println("Observed Result: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase7_InvalidStateTransitions() {
        System.out.println("--- Test Case 7: Invalid State Transitions ---");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1);
            System.out.println("Currently in InsertMoneyState. Trying to add more products via vm.addProducts...");
            vm.addProducts(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("Observed Result (Caught exception): " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase8_MaintenanceMode() {
        System.out.println("--- Test Case 8: Maintenance Mode ---");
        try {
            VendingMachine vm = setupVendingMachine();
            Dispenser dispenser = new Dispenser(2);
            DispenserService ds = new DispenserService(dispenser);
            Denomination d = new Denomination(1, 1, 10);
            DenominationService dns = new DenominationService(new DenominationChainService(d));

            vm.setState(new MaintenanceState(ds, dns));
            System.out.println("In Maintenance Mode. Adding a new product...");
            List<Product> newProducts = new ArrayList<>();
            newProducts.add(new Product(201, "Snickers", 5.0, 50));
            vm.addProducts(newProducts);

            System.out.println("Checking products in maintenance...");
            vm.showProducts();
        } catch (Exception e) {
            System.out.println("Observed Result: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testCase9_EdgeCases() {
        System.out.println("--- Test Case 9: Edge Cases ---");
        try {
            VendingMachine vm = setupVendingMachine();
            System.out.println("Selecting product with ID -1...");
            vm.selectProduct(-1, 1);

            System.out.println("Inserting empty money map...");
            vm.insertMoney(new HashMap<>());
            vm.processTransaction();
        } catch (Exception e) {
            System.out.println("Observed Result: " + e.getMessage());
        }
        System.out.println();
    }
}
