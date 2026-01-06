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

        if (next == null) {
            return true;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        if (money.containsKey(value)) {
            if (money.get(value) + count > denomination.getCapacity()) {
                return false;
            }
            return next.canAddMoney(money);
        }

        return next.canAddMoney(money);
    }

    public boolean addMoney(Map<Integer, Integer> money) {
        if (next == null) {
            return true;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        if (money.containsKey(value)) {
            int needCount = money.get(value);
            if (needCount + count <= denomination.getCapacity()) {
                denomination.addMoney(needCount);
                boolean response = next.addMoney(money);

                if (!response) {
                    denomination.withdrawMoney(needCount);
                    return false;
                }
            }
        }

        return next.addMoney(money);
    }

    public boolean canWithdrawMoney(int amount) {
        if (next == null) {
            return amount == 0;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        if (amount >= value) {
            int needCount = amount / value;
            if (needCount <= count) {
                return next.canWithdrawMoney(amount - (needCount * value));
            } else {
                return false;
            }
        }

        return next.canWithdrawMoney(amount);
    }

    public boolean withdrawMoney(int amount) {
        if (next == null) {
            return amount == 0;
        }

        int value = denomination.getValue();
        int count = denomination.getCount();

        if (amount >= value) {
            int needCount = amount / value;
            if (needCount <= count) {
                denomination.withdrawMoney(needCount);
                boolean response = next.withdrawMoney(amount - (needCount * value));

                if (!response) {
                    denomination.addMoney(needCount);
                    return false;
                }

            } else {
                return false;
            }
        }

        return next.withdrawMoney(amount);
    }

}
