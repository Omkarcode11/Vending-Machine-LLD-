package services;

import java.util.ArrayList;
import java.util.List;

import models.Transaction;

public class TransactionService {
    List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void showTransactions() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public void clearTransactions() {
        this.transactions.clear();
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        this.transactions.add(transaction);
    }

    public Transaction getTransaction(int id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }
        return null;
    }
}
