package services;

import java.util.Map;

import models.Denomination;

public class DenominationChainService {
    Denomination denomination;
    DenominationChainService next;

    public DenominationChainService(Denomination denomination) {
        this.denomination = denomination;
    }

    public void setNext(DenominationChainService next) {
        this.next = next;
    }

    public boolean canAddMoney(Map<Integer, Integer> money) {
        int value = denomination.getValue();
        int count = denomination.getCount();

        if (money.containsKey(value)) {
            if (money.get(value) + count > denomination.getCapacity()) {
                return false;
            }
        }

        if (next == null) {
            return true;
        }

        return next.canAddMoney(money);
    }

    public boolean addMoney(Map<Integer, Integer> money) {
        int value = denomination.getValue();
        int count = denomination.getCount();

        if (money.containsKey(value)) {
            int needCount = money.get(value);
            if (needCount + count <= denomination.getCapacity()) {
                denomination.addMoney(needCount);
            } else {
                return false;
            }
        }

        if (next == null) {
            return true;
        }

        boolean response = next.addMoney(money);
        if (!response && money.containsKey(value)) {
            denomination.withdrawMoney(money.get(value));
        }
        return response;
    }

    public boolean canWithdrawMoney(int amount) {
        if (amount == 0) {
            return true;
        }

        if (amount < 0) {
            return false;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        int takeCount = Math.min(amount / value, count);
        int remaining = amount - (takeCount * value);

        if (next == null) {
            return remaining == 0;
        }

        return next.canWithdrawMoney(remaining);
    }

    public boolean withdrawMoney(int amount) {
        if (amount == 0) {
            return true;
        }

        if (amount < 0) {
            return false;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        int takeCount = Math.min(amount / value, count);
        int remaining = amount - (takeCount * value);

        if (next == null) {
            return remaining == 0;
        }

        denomination.withdrawMoney(takeCount);
        boolean response = next.withdrawMoney(remaining);

        if (!response) {
            denomination.addMoney(takeCount);
            return false;
        }

        return true;
    }

}
