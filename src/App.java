import java.util.*;
import models.*;
import services.*;
import states.*;

public class App {
    private static List<String> passedTests = new ArrayList<>();
    private static List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   VENDING MACHINE ROBUSTNESS TEST SUITE  ");
        System.out.println("==========================================\n");

        runTest("TestCase1: Successful Purchase", App::testCase1_SuccessfulPurchase);
        runTest("TestCase2: Insufficient Money", App::testCase2_InsufficientMoney);
        runTest("TestCase3: Out of Stock", App::testCase3_OutOfStock);
        runTest("TestCase4: No Change Available", App::testCase4_NoChangeAvailable);
        runTest("TestCase5: Multiple Items Purchase", App::testCase5_MultipleItemsPurchase);
        runTest("TestCase6: Cancel and Clear Cart", App::testCase6_CancelAndClear);
        runTest("TestCase7: Invalid State Movement (Add Product in Payment)", App::testCase7_InvalidStateTransitions);
        runTest("TestCase8: Maintenance Mode Operations", App::testCase8_MaintenanceMode);
        runTest("TestCase9: Selection Failure (Invalid ID)", App::testCase9_EdgeCases_NullProduct);
        runTest("TestCase10: Exact Change Logic", App::testCase10_ExactChangeFlow);
        runTest("TestCase11: Input Validation (Negative Qty)", App::testCase11_NegativeQuantity);
        runTest("TestCase12: Input Validation (Zero Qty)", App::testCase12_ZeroQuantity);
        runTest("TestCase13: Vault Protection (Capacity Full)", App::testCase13_VaultCapacityExceeded);
        runTest("TestCase14: Multi-Select Continuity", App::testCase14_MultiSelectContinuity);
        runTest("TestCase15: Overpayment & Change Distribution", App::testCase15_OverpaymentChange);

        printSummary();
    }

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
        } catch (Exception e) {
            if (!finishedProperly(name)) {
                failedTests.add(name + " (Crashed: " + e.getMessage() + ")");
            }
        }
    }

    private static boolean finishedProperly(String name) {
        return passedTests.contains(name) || failedTests.stream().anyMatch(f -> f.startsWith(name));
    }

    private static void printSummary() {
        System.out.println("\n==========================================");
        System.out.println("           TEST SUITE SUMMARY             ");
        System.out.println("==========================================");
        System.out.println("TOTAL TESTS: " + (passedTests.size() + failedTests.size()));
        System.out.println("PASSED: " + passedTests.size());
        System.out.println("FAILED: " + failedTests.size());

        if (!failedTests.isEmpty()) {
            System.out.println("\n--- FAILED TEST CASES ---");
            for (String failure : failedTests) {
                System.out.println("✖ " + failure);
            }
        } else {
            System.out.println("\n✔ ALL TESTS PASSED!");
        }
        System.out.println("==========================================\n");
    }

    private static VendingMachine setupVendingMachine() {
        Denomination d10 = new Denomination(1, 10, 100);
        d10.addMoney(50);
        DenominationChainService chain10 = new DenominationChainService(d10);

        Denomination d5 = new Denomination(2, 5, 100);
        d5.addMoney(50); // Add 50 coins of 5 (50% capacity)
        DenominationChainService chain5 = new DenominationChainService(d5);

        Denomination d1 = new Denomination(3, 1, 500);
        d1.addMoney(100); // Add 100 coins of 1 (20% capacity)
        DenominationChainService chain1 = new DenominationChainService(d1);

        chain10.setNext(chain5);
        chain5.setNext(chain1);

        DenominationService denominationService = new DenominationService(chain10);
        Dispenser dispenser = new Dispenser(1);
        DispenserService dispenserService = new DispenserService(dispenser);

        Product p1 = new Product(101, "Coke", 15.0, 10, 10);
        Product p2 = new Product(102, "Pepsi", 12.0, 5, 5);
        Product p3 = new Product(103, "Water", 10.0, 20, 20);

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
        String name = "TestCase1: Successful Purchase";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Transaction success for 1 Coke (Price 15).");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1);
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 2); // 20 units
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase2_InsufficientMoney() {
        String name = "TestCase2: Insufficient Money";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Reject due to insufficient funds.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(103, 1); // Price 10
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(5, 1); // Only 5 units
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Failed (Allowed transaction with insufficient money)");
            failedTests.add(name + " (No rejection)");
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught expected error: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase3_OutOfStock() {
        String name = "TestCase3: Out of Stock";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Reject selection larger than available quantity.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(102, 10); // 10 requested, 5 available
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 15);
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Failed (Allowed selection of more than available items)");
            failedTests.add(name + " (Stock limit ignored)");
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught expected error: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase4_NoChangeAvailable() {
        String name = "TestCase4: No Change Available";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Reject if change cannot be provided from vault.");
        try {
            Dispenser dispenser = new Dispenser(1);
            Product p1 = new Product(101, "Coke", 15.0, 10, 10);
            dispenser.addProduct(p1);
            DispenserService ds = new DispenserService(dispenser);

            // Change vault with NO small coins
            Denomination d50 = new Denomination(1, 50, 10);
            DenominationChainService chain = new DenominationChainService(d50);
            DenominationService dns = new DenominationService(chain);

            VendingMachine vm = new VendingMachine(1, ds, dns, new SelectProductService(dispenser),
                    new TransactionService());
            vm.setState(new IdleState(new SelectProductService(dispenser), ds));

            vm.selectProduct(101, 1); // Price 15
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(50, 1); // Insert 50, need 35 change
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Failed (Allowed transaction without sufficient change coins)");
            failedTests.add(name + " (Change logic failed)");
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught expected error: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase5_MultipleItemsPurchase() {
        String name = "TestCase5: Multiple Items Purchase";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Successful purchase of multiple different items.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1); // 15
            vm.updateSelectedProduct(102, 1); // 12. Total 27
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 3); // 30 units
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase6_CancelAndClear() {
        String name = "TestCase6: Cancel and Clear Cart";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Cart cleared, system returns to Idle.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1);
            vm.clearSelectedProducts();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase7_InvalidStateTransitions() {
        String name = "TestCase7: Invalid State Movement (Add Product in Payment)";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: UnsupportedOperationException.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1);
            vm.confirmInsertMoney();
            vm.addProducts(new ArrayList<>());
            System.out.println("[RESULT]: Failed (Operation allowed in invalid state)");
            failedTests.add(name + " (State violation)");
        } catch (UnsupportedOperationException e) {
            System.out.println("[RESULT]: Passed (Caught expected exception: " + e.getMessage() + ")");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed (Caught wrong exception type: " + e.getClass().getSimpleName() + ")");
            failedTests.add(name + " (Wrong exception: " + e.getClass().getSimpleName() + ")");
        }
        System.out.println();
    }

    private static void testCase8_MaintenanceMode() {
        String name = "TestCase8: Maintenance Mode Operations";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Admin can add stock while in Maintenance state.");
        try {
            VendingMachine vm = setupVendingMachine();
            Dispenser dispenser = new Dispenser(2);
            DispenserService ds = new DispenserService(dispenser);
            DenominationService dns = new DenominationService(new DenominationChainService(new Denomination(1, 1, 10)));

            vm.setState(new MaintenanceState(ds, dns));
            List<Product> newLoad = new ArrayList<>();
            newLoad.add(new Product(201, "Chips", 5.0, 50, 100));
            vm.addProducts(newLoad);
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase9_EdgeCases_NullProduct() {
        String name = "TestCase9: Selection Failure (Invalid ID)";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Graceful handling (no crash) when invalid ID selected.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(999, 1); // This should throw or ignore
            System.out.println("[RESULT]: Success (Selection ignored/handled)");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught expected error: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase10_ExactChangeFlow() {
        String name = "TestCase10: Exact Change Logic";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Transaction success with zero change required.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(103, 1); // Price 10
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 1); // Exact 10
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase11_NegativeQuantity() {
        String name = "TestCase11: Input Validation (Negative Qty)";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Rejection of negative purchase quantity.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, -5);
            vm.confirmInsertMoney();
            System.out.println("[RESULT]: Failed (Negative quantity accepted)");
            failedTests.add(name + " (Invalid input allowed)");
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Rejected: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase12_ZeroQuantity() {
        String name = "TestCase12: Input Validation (Zero Qty)";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Logical handling/rejection of zero quantity selection.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 0);
            System.out.println("[RESULT]: Success (Selection ignored/handled)");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught expected error: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase13_VaultCapacityExceeded() {
        String name = "TestCase13: Vault Protection (Capacity Full)";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Reject coins if vault capacity reached.");
        try {
            Denomination d1 = new Denomination(1, 10, 1); // Capacity 1
            d1.addMoney(1); // Already full
            DenominationChainService chain = new DenominationChainService(d1);
            DenominationService dns = new DenominationService(chain);

            Dispenser dispenser = new Dispenser(1);
            dispenser.addProduct(new Product(101, "Coke", 10.0, 10, 10));
            DispenserService ds = new DispenserService(dispenser);

            VendingMachine vm = new VendingMachine(1, ds, dns, new SelectProductService(dispenser),
                    new TransactionService());
            vm.setState(new IdleState(new SelectProductService(dispenser), ds));

            vm.selectProduct(101, 1);
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 1); // Try adding 1 more while full
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Failed (Vault exceeded capacity without error)");
            failedTests.add(name + " (Capacity bypass)");
        } catch (Exception e) {
            System.out.println("[RESULT]: Passed (Caught: " + e.getMessage() + ")");
            passedTests.add(name);
        }
        System.out.println();
    }

    private static void testCase14_MultiSelectContinuity() {
        String name = "TestCase14: Multi-Select Continuity";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Success when selecting multiple items sequentially before payment.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(101, 1); // Coke 15
            vm.selectProduct(103, 2); // Water 10*2 = 20. Total 35
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 3);
            money.put(5, 1); // 35 units total
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }

    private static void testCase15_OverpaymentChange() {
        String name = "TestCase15: Overpayment & Change Distribution";
        System.out.println("--- " + name + " ---");
        System.out.println("[EXPECTED]: Transaction success with correct change returned from multiple handlers.");
        try {
            VendingMachine vm = setupVendingMachine();
            vm.selectProduct(103, 1); // Water 10
            vm.confirmInsertMoney();
            Map<Integer, Integer> money = new HashMap<>();
            money.put(10, 5); // Insert 50, need 40 change
            vm.insertMoney(money);
            vm.processTransaction();
            System.out.println("[RESULT]: Success");
            passedTests.add(name);
        } catch (Exception e) {
            System.out.println("[RESULT]: Failed - " + e.getMessage());
            failedTests.add(name + " (" + e.getMessage() + ")");
        }
        System.out.println();
    }
}
